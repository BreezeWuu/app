package com.zotye.wms.data.binding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by hechuangju on 2017/8/31 下午5:50.
 */
public class FragmentBindingAdapters {

    @BindingAdapter(value = {"glide_do_animate", "glide_imageUrl", "glide_placeholder", "glide_error"}, requireAll = false)
    public static void loadImage(ImageView view, boolean doAnimate, String url, int placeholder, int error) {
        if (view != null) {
            DrawableRequestBuilder builder = Glide.with(view.getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.ALL);
            if (!doAnimate) builder.dontAnimate();
            if (placeholder > 0)
                builder.placeholder(placeholder);
            if (error > 0)
                builder.error(error);
            builder.into(view);
        }
    }
}