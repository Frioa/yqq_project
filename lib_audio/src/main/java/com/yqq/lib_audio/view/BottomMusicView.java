package com.yqq.lib_audio.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yqq.lib_audio.R;
import com.yqq.lib_audio.core.AudioController;
import com.yqq.lib_audio.event.*;
import com.yqq.lib_audio.model.AudioBean;
import com.yqq.lib_image_loader.app.ImageLoaderManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 播放器底部View
 */
public class BottomMusicView extends RelativeLayout {

  private static String TAG = "BottomMusicView";
  private Context mContext;

  /*
   * View
   */
  private ImageView mLeftView;
  private TextView mTitleView;
  private TextView mAlbumView;
  private ImageView mPlayView;
  private ImageView mRightView;
  /*
   * data
   */
  private AudioBean mAudioBean;

  public BottomMusicView(Context context) {
    this(context, null);
  }

  public BottomMusicView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BottomMusicView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mContext = context;
    EventBus.getDefault().register(this);
    initView();
  }

  private void initView() {
    View rootView = LayoutInflater.from(mContext).inflate(R.layout.bottom_view, this);

    rootView.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        //跳到音乐播放Activitity
//        MusicPlayerActivity.start((Activity) mContext);
        Log.d("TAG", "跳到音乐播放Activitity");

      }
    });
    mLeftView = rootView.findViewById(R.id.album_view);
    // 让左侧 mLeftView 不停旋转
    // TODO: 暂停时暂停，播放时选装
    ObjectAnimator animator = ObjectAnimator.ofFloat(mLeftView, View.ROTATION.getName(), 0f, 360);
    animator.setDuration(10000);
    animator.setInterpolator(new LinearInterpolator());
    animator.setRepeatCount(-1);
    animator.start();

    mTitleView = rootView.findViewById(R.id.audio_name_view);
    mAlbumView = rootView.findViewById(R.id.audio_album_view);
    mPlayView = rootView.findViewById(R.id.play_view);

    mPlayView.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        //处理播放暂停事件
        AudioController.instance().playOrPause();
//        AudioController.instance().play();
      }
    });
    mRightView = rootView.findViewById(R.id.show_list_view);
    mRightView.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        //显示音乐列表对话框
    /*    MusicListDialog dialog = new MusicListDialog(mContext);
        dialog.show();*/
      }
    });

  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    EventBus.getDefault().unregister(this);
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void onAudioLoadEvent(AudioLoadEvent event) {
    //更新当前view为load状态
    mAudioBean = event.getMAudioBean();
    showLoadView();
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void onAudioPauseEvent(AudioPauseEvent event) {
    //更新当前view为暂停状态
    showPauseView();
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void onAudioStartEvent(AudioStartEvent event) {
    //更新当前view为播放状态
    showPlayView();
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onAudioProgrssEvent(AudioProgressEvent event) {
    //更新当前view的播放进度
  }

  private void showLoadView() {
    //目前loading状态的UI处理与pause逻辑一样，分开为了以后好扩展
    if (mAudioBean != null) {
      ImageLoaderManager.getInstance().displayImageForCircle(mLeftView, mAudioBean.albumPic);
      mTitleView.setText(mAudioBean.name);
      mAlbumView.setText(mAudioBean.album);
      mPlayView.setImageResource(R.mipmap.note_btn_pause_white);
    }
  }

  private void showPauseView() {
    if (mAudioBean != null) {
      mPlayView.setImageResource(R.mipmap.note_btn_play_white);
    }
  }

  private void showPlayView() {
    if (mAudioBean != null) {
      mPlayView.setImageResource(R.mipmap.note_btn_pause_white);
    }
  }
}
