/*
 * Copyright 2013, Edmodo, Inc. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License.
 * You may obtain a copy of the License in the LICENSE file, or at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" 
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License. 
 */

package com.hb.rssai.zxing;




import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hb.rssai.R;


/**
 * Custom view that provides cropping capabilities to an image.
 */
public class CropImageView extends FrameLayout {

    // Member Variables ////////////////////////////////////////////////////////

    // Sets the default image guidelines to show when resizing
    public static final int DEFAULT_GUIDELINES = 1;
    public static final boolean DEFAULT_FIXED_ASPECT_RATIO = true;	//
    public static final int DEFAULT_ASPECT_RATIO_X = 1;
    public static final int DEFAULT_ASPECT_RATIO_Y = 1;

    private static final int DEFAULT_IMAGE_RESOURCE = 0;
    
    private static final String DEGREES_ROTATED = "DEGREES_ROTATED";

    private ImageView mImageView;
    private CropOverlayView mCropOverlayView;

    private Bitmap mBitmap;
    private int mDegreesRotated = 0;

    private int mLayoutWidth;
    private int mLayoutHeight;

    // Instance variables for customizable attributes
    private int mGuidelines = DEFAULT_GUIDELINES;
    private boolean mFixAspectRatio = DEFAULT_FIXED_ASPECT_RATIO;
    private int mAspectRatioX = DEFAULT_ASPECT_RATIO_X;
    private int mAspectRatioY = DEFAULT_ASPECT_RATIO_Y;
    private int mImageResource = DEFAULT_IMAGE_RESOURCE;

    // Constructors ////////////////////////////////////////////////////////////

    public CropImageView(Context context) {
        super(context);
        init(context);
    }

    public CropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //
        /*TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CropImageView, 0, 0);

        try {
            mGuidelines = ta.getInteger(R.styleable.CropImageView_guidelines, DEFAULT_GUIDELINES);
            mFixAspectRatio = ta.getBoolean(R.styleable.CropImageView_fixAspectRatio, DEFAULT_FIXED_ASPECT_RATIO);
            mAspectRatioX = ta.getInteger(R.styleable.CropImageView_aspectRatioX, DEFAULT_ASPECT_RATIO_X);
            mAspectRatioY = ta.getInteger(R.styleable.CropImageView_aspectRatioY, DEFAULT_ASPECT_RATIO_Y);
            mImageResource = ta.getResourceId(R.styleable.CropImageView_imageResource, DEFAULT_IMAGE_RESOURCE);

        } finally {
            ta.recycle();
        }*/

        init(context);
    }

    
    // View Methods ////////////////////////////////////////////////////////////

    @Override
    public Parcelable onSaveInstanceState() {

        final Bundle bundle = new Bundle();

        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt(DEGREES_ROTATED, mDegreesRotated);
        
        return bundle;
        
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {

            final Bundle bundle = (Bundle) state;

            //Fixes the rotation of the image when orientation changes.
            mDegreesRotated = bundle.getInt(DEGREES_ROTATED);
            int tempDegrees = mDegreesRotated;
            rotateImage(mDegreesRotated);
            mDegreesRotated = tempDegrees;
            
            super.onRestoreInstanceState(bundle.getParcelable("instanceState"));

        } else {
            super.onRestoreInstanceState(state);
        }
    }
    
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        if (mBitmap != null) {
            final Rect bitmapRect = ImageViewUtil.getBitmapRectCenterInside(mBitmap, this);
            mCropOverlayView.setBitmapRect(bitmapRect);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (mBitmap != null) {

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            // Bypasses a baffling bug when used within a ScrollView, where
            // heightSize is set to 0.
            if (heightSize == 0)
                heightSize = mBitmap.getHeight();

            int desiredWidth;
            int desiredHeight;

            double viewToBitmapWidthRatio = Double.POSITIVE_INFINITY;
            double viewToBitmapHeightRatio = Double.POSITIVE_INFINITY;

            // Checks if either width or height needs to be fixed
            if (widthSize < mBitmap.getWidth()) {
                viewToBitmapWidthRatio = (double) widthSize / (double) mBitmap.getWidth();
            }
            if (heightSize < mBitmap.getHeight()) {
                viewToBitmapHeightRatio = (double) heightSize / (double) mBitmap.getHeight();
            }

            // If either needs to be fixed, choose smallest ratio and calculate
            // from
            // there
            if (viewToBitmapWidthRatio != Double.POSITIVE_INFINITY || viewToBitmapHeightRatio != Double.POSITIVE_INFINITY)
            {
                if (viewToBitmapWidthRatio <= viewToBitmapHeightRatio) {
                    desiredWidth = widthSize;
                    desiredHeight = (int) (mBitmap.getHeight() * viewToBitmapWidthRatio);
                }
                else {
                    desiredHeight = heightSize;
                    desiredWidth = (int) (mBitmap.getWidth() * viewToBitmapHeightRatio);
                }
            }

            // Otherwise, the picture is within frame layout bounds. Desired
            // width
            // is simply picture size
            else {
                desiredWidth = mBitmap.getWidth();
                desiredHeight = mBitmap.getHeight();
            }

            int width = getOnMeasureSpec(widthMode, widthSize, desiredWidth);
            int height = getOnMeasureSpec(heightMode, heightSize, desiredHeight);

            mLayoutWidth = width;
            mLayoutHeight = height;

            final Rect bitmapRect = ImageViewUtil.getBitmapRectCenterInside(mBitmap.getWidth(),
                                                                            mBitmap.getHeight(),
                                                                            mLayoutWidth,
                                                                            mLayoutHeight);
            mCropOverlayView.setBitmapRect(bitmapRect);

            // MUST CALL THIS
            setMeasuredDimension(mLayoutWidth, mLayoutHeight);

        } else {

            setMeasuredDimension(widthSize, heightSize);
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        
        super.onLayout(changed, l, t, r, b);

        // Gets original parameters, and creates the new parameters
        ViewGroup.LayoutParams origparams = (ViewGroup.LayoutParams) this.getLayoutParams();
        origparams.width = mLayoutWidth;
        origparams.height = mLayoutHeight;

        setLayoutParams(origparams);
    }

    // Public Methods //////////////////////////////////////////////////////////

    /**
     * Returns the integer of the imageResource
     * 
     *
     */
    public int getImageResource() {
        return mImageResource;
    }

    /**
     * Sets a Bitmap as the content of the CropImageView.
     * 
     * @param bitmap the Bitmap to set
     */
    public void setImageBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        mImageView.setImageBitmap(mBitmap);

        if (mCropOverlayView != null) {
            mCropOverlayView.resetCropOverlayView();
        }
    }

    /**
     * Sets a Drawable as the content of the CropImageView.
     * 
     * @param resId the drawable resource ID to set
     */
    public void setImageResource(int resId) {
        if (resId != 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
            setImageBitmap(bitmap);
        }
    }

    /**
     * Gets the cropped image based on the current crop window.
     * 
     * @return a new Bitmap representing the cropped image
     */
    public Bitmap getCroppedImage() {

        final Rect displayedImageRect = ImageViewUtil.getBitmapRectCenterInside(mBitmap, mImageView);

        // Get the scale factor between the actual Bitmap dimensions and the
        // displayed dimensions for width.
        final float actualImageWidth = mBitmap.getWidth();
        final float displayedImageWidth = displayedImageRect.width();
        final float scaleFactorWidth = actualImageWidth / displayedImageWidth;

        // Get the scale factor between the actual Bitmap dimensions and the
        // displayed dimensions for height.
        final float actualImageHeight = mBitmap.getHeight();
        final float displayedImageHeight = displayedImageRect.height();
        final float scaleFactorHeight = actualImageHeight / displayedImageHeight;

        // Get crop window position relative to the displayed image.
        final float cropWindowX = Edge.LEFT.getCoordinate() - displayedImageRect.left;
        final float cropWindowY = Edge.TOP.getCoordinate() - displayedImageRect.top;
        final float cropWindowWidth = Edge.getWidth();
        final float cropWindowHeight = Edge.getHeight();

        // Scale the crop window position to the actual size of the Bitmap.
        final float actualCropX = cropWindowX * scaleFactorWidth;
        final float actualCropY = cropWindowY * scaleFactorHeight;
        final float actualCropWidth = cropWindowWidth * scaleFactorWidth;
        final float actualCropHeight = cropWindowHeight * scaleFactorHeight;

        // Crop the subset from the original Bitmap.
        final Bitmap croppedBitmap = Bitmap.createBitmap(mBitmap,
                                                         (int) actualCropX,
                                                         (int) actualCropY,
                                                         (int) actualCropWidth,
                                                         (int) actualCropHeight);

        return croppedBitmap;
    }

    /**
     * Sets whether the aspect ratio is fixed or not; true fixes the aspect
     * ratio, while false allows it to be changed.
     * 
     * @param fixAspectRatio Boolean that signals whether the aspect ratio
     *            should be maintained.
     */
    public void setFixedAspectRatio(boolean fixAspectRatio)
    {
        mCropOverlayView.setFixedAspectRatio(fixAspectRatio);
    }

    /**
     * Sets the guidelines for the CropOverlayView to be either on, off, or to
     * show when resizing the application.
     * 
     * @param guidelines Integer that signals whether the guidelines should be
     *            on, off, or only showing when resizing.
     */
    public void setGuidelines(int guidelines)
    {
        mCropOverlayView.setGuidelines(guidelines);
    }

    /**
     * Sets the both the X and Y values of the aspectRatio.
     * 
     * @param aspectRatioX int that specifies the new X value of the aspect
     *            ratio
     * @param aspectRatioX int that specifies the new Y value of the aspect
     *            ratio
     */
    public void setAspectRatio(int aspectRatioX, int aspectRatioY)
    {
        mAspectRatioX = aspectRatioX;
        mCropOverlayView.setAspectRatioX(mAspectRatioX);

        mAspectRatioY = aspectRatioY;
        mCropOverlayView.setAspectRatioY(mAspectRatioY);
    }
    
    /**
     * Rotates image by the specified number of degrees clockwise. Cycles from 0 to 360 degrees.
     * 
     * @param degrees Integer specifying the number of degrees to rotate.
     */
    public void rotateImage(int degrees) {
    	
    	if(mBitmap==null){
    		return;
    	}
    	
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
        setImageBitmap(mBitmap);
        
        mDegreesRotated += degrees;
        mDegreesRotated = mDegreesRotated % 360;
    }
    
    

    // Private Methods /////////////////////////////////////////////////////////

    private void init(Context context) {

        final LayoutInflater inflater = LayoutInflater.from(context);
        final View v = inflater.inflate(R.layout.crop_image_view, this, true);

        mImageView = (ImageView) v.findViewById(R.id.ImageView_image);

        setImageResource(mImageResource);
        mCropOverlayView = (CropOverlayView) v.findViewById(R.id.CropOverlayView);
        mCropOverlayView.setInitialAttributeValues(mGuidelines, mFixAspectRatio, mAspectRatioX, mAspectRatioY);

    }

    /**
     * Determines the specs for the onMeasure function. Calculates the width or
     * height depending on the mode.
     * 
     * @param measureSpecMode The mode of the measured width or height.
     * @param measureSpecSize The size of the measured width or height.
     * @param desiredSize The desired size of the measured width or height.
     * @return The final size of the width or height.
     */
    private static int getOnMeasureSpec(int measureSpecMode, int measureSpecSize, int desiredSize)
    {
        // Measure Width
        int spec;
        if (measureSpecMode == MeasureSpec.EXACTLY) {
            // Must be this size
            spec = measureSpecSize;
        } else if (measureSpecMode == MeasureSpec.AT_MOST) {
            // Can't be bigger than...; match_parent value
            spec = Math.min(desiredSize, measureSpecSize);
        } else {
            // Be whatever you want; wrap_content
            spec = desiredSize;
        }

        return spec;
    }

}