package com.lh.ch.hefenglocation.photo;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.PopupWindow;

import uk.co.senab.photoview.DefaultOnDoubleTapListener;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by CH on 2017/6/1.
 */

public class MyOnDoubleTapListener extends DefaultOnDoubleTapListener {
    private PopupWindow mPopupWindow;
    private Context mContext;

    /**
     * Default constructor
     *
     * @param photoViewAttacher PhotoViewAttacher to bind to
     */
    public MyOnDoubleTapListener(PhotoViewAttacher photoViewAttacher, Context mContext, PopupWindow mPopupWindow) {
        super(photoViewAttacher);
        this.mContext = mContext;
        this.mPopupWindow = mPopupWindow;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d("test", "onSingleTapConfirmed: ");
        DRAppUtil.getInstance().showSystemUI(mPopupWindow.getContentView());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException mE) {
                    mE.printStackTrace();
                }
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPopupWindow.dismiss();
                    }
                });
            }
        }).start();

        return super.onSingleTapConfirmed(e);
    }
}