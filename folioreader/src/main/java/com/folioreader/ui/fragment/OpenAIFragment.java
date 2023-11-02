package com.folioreader.ui.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.folioreader.Config;
import com.folioreader.Constants;
import com.folioreader.R;
import com.folioreader.util.AppUtil;
import com.folioreader.util.UiUtil;
import com.squareup.picasso.Picasso;

public class OpenAIFragment extends DialogFragment {

    private static final String TAG = "OpenAIFragment";

    private String url;
    private ProgressBar progressBar;
    private ImageView imageViewClose, imageViewPhoto;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
        url = getArguments().getString(Constants.URL_IMAGE_OPEN_AI);
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_open_ai, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = (ProgressBar) view.findViewById(R.id.progress);

        imageViewClose = view.findViewById(R.id.btn_close);
        imageViewPhoto = view.findViewById(R.id.iv_photo);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



        configureTheme(view);
    }

    private void configureTheme(View view) {

        Config config = AppUtil.getSavedConfig(getContext());
        assert config != null;
        assert getContext() != null;
        final int themeColor = config.getCurrentThemeColor();

        UiUtil.setColorIntToDrawable(themeColor, imageViewClose.getDrawable());
        LinearLayout layoutHeader = view.findViewById(R.id.layout_header);
        layoutHeader.setBackgroundDrawable(UiUtil.getShapeDrawable(themeColor));
        UiUtil.setColorIntToDrawable(themeColor, progressBar.getIndeterminateDrawable());

        if (config.isNightMode()) {
            view.findViewById(R.id.toolbar).setBackgroundColor(Color.BLACK);
            view.findViewById(R.id.contentView).setBackgroundColor(Color.BLACK);

        } else {
            view.findViewById(R.id.contentView).setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public void onOpenAIDataReceived(String url) {
        progressBar.setVisibility(View.GONE);
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.red_border_background)
                .error(R.drawable.ic_offline_gray_48dp)
                .fit()
                .into(imageViewPhoto);
    }
}
