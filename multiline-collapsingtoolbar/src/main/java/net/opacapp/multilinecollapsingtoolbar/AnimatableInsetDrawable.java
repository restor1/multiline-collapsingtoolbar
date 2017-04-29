package net.opacapp.multilinecollapsingtoolbar;

/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

    import android.annotation.TargetApi;
    import android.content.res.Resources;
    import android.graphics.Canvas;
    import android.graphics.ColorFilter;
    import android.graphics.Outline;
    import android.graphics.Rect;
    import android.graphics.drawable.Drawable;
    import android.os.Build;

/**
 * A Drawable that insets another Drawable by a specified distance.
 * This is used when a View needs a background that is smaller than
 * the View's actual bounds.
 * A modified copy of the original Android InsetDrawable. The original had no method to change the insets,
 * only the bounds. The problem with the bounds would be that the click region would be horrible.
 * Original source: http://grepcode.com/file_/repository.grepcode.com/java/ext/com.google.android/android/5.0.2_r1/android/graphics/drawable/InsetDrawable.java/?v=source
 */
public class AnimatableInsetDrawable extends Drawable implements Drawable.Callback {
  private int _drawOffset = 0;

  private final Rect mTmpRect = new Rect();

  private InsetState mInsetState;
  private boolean mMutated;

  /*package*/ AnimatableInsetDrawable() {
    this(null, null);
  }

  public AnimatableInsetDrawable(Drawable drawable, int inset) {
    this(drawable, inset, inset, inset, inset);
  }

  public AnimatableInsetDrawable(Drawable drawable, int insetLeft, int insetTop,
      int insetRight, int insetBottom) {
    this(null, null);

    mInsetState.mDrawable = drawable;
    mInsetState.mInsetLeft = insetLeft;
    mInsetState.mInsetTop = insetTop;
    mInsetState.mInsetRight = insetRight;
    mInsetState.mInsetBottom = insetBottom;

    if (drawable != null) {
      drawable.setCallback(this);
    }
  }


  /**
   * Changes the insets of the drawable to make it animatable.
   *
   * @param insetLeft   The left inset.
   * @param insetTop    The top inset.
   * @param insetRight  The right inset.
   * @param insetBottom The bottom inset.
   */
  public void setInsets(int insetLeft, int insetTop, int insetRight, int insetBottom) {
    mInsetState.mInsetLeft = insetLeft;
    mInsetState.mInsetTop = insetTop;
    mInsetState.mInsetRight = insetRight;
    mInsetState.mInsetBottom = insetBottom;
    onBoundsChange(getBounds());
  }

  // TODO add getInsets()

  @Override
  public boolean canApplyTheme() {
    return mInsetState != null && mInsetState.mThemeAttrs != null;
  }

  @Override
  public void invalidateDrawable(Drawable who) {
    final Callback callback = getCallback();
    if (callback != null) {
      callback.invalidateDrawable(this);
    }
  }

  @Override
  public void scheduleDrawable(Drawable who, Runnable what, long when) {
    final Callback callback = getCallback();
    if (callback != null) {
      callback.scheduleDrawable(this, what, when);
    }
  }

  @Override
  public void unscheduleDrawable(Drawable who, Runnable what) {
    final Callback callback = getCallback();
    if (callback != null) {
      callback.unscheduleDrawable(this, what);
    }
  }

  @Override
  public void draw(Canvas canvas) {
    canvas.save();
    canvas.translate(_drawOffset, 0);
    mInsetState.mDrawable.draw(canvas);
    canvas.restore();
  }

  @Override
  public int getChangingConfigurations() {
    return super.getChangingConfigurations()
        | mInsetState.mChangingConfigurations
        | mInsetState.mDrawable.getChangingConfigurations();
  }

  @Override
  public boolean getPadding(Rect padding) {
    boolean pad = mInsetState.mDrawable.getPadding(padding);

    padding.left += mInsetState.mInsetLeft;
    padding.right += mInsetState.mInsetRight;
    padding.top += mInsetState.mInsetTop;
    padding.bottom += mInsetState.mInsetBottom;

    return pad || (mInsetState.mInsetLeft | mInsetState.mInsetRight |
        mInsetState.mInsetTop | mInsetState.mInsetBottom) != 0;
  }


  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @Override
  public void setHotspot(float x, float y) {
    mInsetState.mDrawable.setHotspot(x, y);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @Override
  public void setHotspotBounds(int left, int top, int right, int bottom) {
    mInsetState.mDrawable.setHotspotBounds(left, top, right, bottom);
  }

  @Override
  public boolean setVisible(boolean visible, boolean restart) {
    mInsetState.mDrawable.setVisible(visible, restart);
    return super.setVisible(visible, restart);
  }

  @Override
  public void setAlpha(int alpha) {
    mInsetState.mDrawable.setAlpha(alpha);
  }

  @TargetApi(Build.VERSION_CODES.KITKAT)
  @Override
  public int getAlpha() {
    return mInsetState.mDrawable.getAlpha();
  }

  @Override
  public void setColorFilter(ColorFilter cf) {
    mInsetState.mDrawable.setColorFilter(cf);
  }


  @Override
  public int getOpacity() {
    return mInsetState.mDrawable.getOpacity();
  }

  @Override
  public boolean isStateful() {
    return mInsetState.mDrawable.isStateful();
  }

  @Override
  protected boolean onStateChange(int[] state) {
    boolean changed = mInsetState.mDrawable.setState(state);
    onBoundsChange(getBounds());
    return changed;
  }

  @Override
  protected boolean onLevelChange(int level) {
    return mInsetState.mDrawable.setLevel(level);
  }

  @Override
  protected void onBoundsChange(Rect bounds) {
    final Rect r = mTmpRect;
    r.set(bounds);

    r.left += mInsetState.mInsetLeft;
    r.top += mInsetState.mInsetTop;
    r.right -= mInsetState.mInsetRight;
    r.bottom -= mInsetState.mInsetBottom;

    mInsetState.mDrawable.setBounds(r.left, r.top, r.right, r.bottom);
  }

  @Override
  public int getIntrinsicWidth() {
    return mInsetState.mDrawable.getIntrinsicWidth();
  }

  @Override
  public int getIntrinsicHeight() {
    return mInsetState.mDrawable.getIntrinsicHeight();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @Override
  public void getOutline(Outline outline) {
    mInsetState.mDrawable.getOutline(outline);
  }

  @Override
  public ConstantState getConstantState() {
    if (mInsetState.canConstantState()) {
      mInsetState.mChangingConfigurations = getChangingConfigurations();
      return mInsetState;
    }
    return null;
  }

  @Override
  public Drawable mutate() {
    if (!mMutated && super.mutate() == this) {
      mInsetState.mDrawable.mutate();
      mMutated = true;
    }
    return this;
  }

  /**
   * Returns the drawable wrapped by this InsetDrawable. May be null.
   *
   * @return The wrapped drawable.
   */
  public Drawable getDrawable() {
    return mInsetState.mDrawable;
  }

  public void notifyDrawWithOffset(int offset) {
    _drawOffset = offset;
  }

  final static class InsetState extends ConstantState {
    int[] mThemeAttrs;
    int mChangingConfigurations;

    Drawable mDrawable;

    int mInsetLeft;
    int mInsetTop;
    int mInsetRight;
    int mInsetBottom;

    boolean mCheckedConstantState;
    boolean mCanConstantState;

    InsetState(InsetState orig, AnimatableInsetDrawable owner, Resources res) {
      if (orig != null) {
        mThemeAttrs = orig.mThemeAttrs;
        mChangingConfigurations = orig.mChangingConfigurations;
        if (res != null) {
          mDrawable = orig.mDrawable.getConstantState().newDrawable(res);
        } else {
          mDrawable = orig.mDrawable.getConstantState().newDrawable();
        }
        mDrawable.setCallback(owner);
        mDrawable.setBounds(orig.mDrawable.getBounds());
        mDrawable.setLevel(orig.mDrawable.getLevel());
        mInsetLeft = orig.mInsetLeft;
        mInsetTop = orig.mInsetTop;
        mInsetRight = orig.mInsetRight;
        mInsetBottom = orig.mInsetBottom;
        mCheckedConstantState = mCanConstantState = true;
      }
    }

    @Override
    public Drawable newDrawable() {
      return new AnimatableInsetDrawable(this, null);
    }

    @Override
    public Drawable newDrawable(Resources res) {
      return new AnimatableInsetDrawable(this, res);
    }

    @Override
    public int getChangingConfigurations() {
      return mChangingConfigurations;
    }

    boolean canConstantState() {
      if (!mCheckedConstantState) {
        mCanConstantState = mDrawable.getConstantState() != null;
        mCheckedConstantState = true;
      }

      return mCanConstantState;
    }
  }

  private AnimatableInsetDrawable(InsetState state, Resources res) {
    mInsetState = new InsetState(state, this, res);
  }
}