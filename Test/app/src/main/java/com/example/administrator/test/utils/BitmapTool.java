package com.example.administrator.test.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;

/**
 * Create by zmm
 * Time 2019/5/29
 * PackageName com.example.administrator.test.utils
 */
public class BitmapTool {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap toMoHu(Bitmap bitmap, Context context) {
        Bitmap blurredBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        RenderScript rs = RenderScript.create(context);
        Allocation input = Allocation.createFromBitmap(rs, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setInput(input);

        //设置模糊半径
        script.setRadius(25);

        script.forEach(output);

        output.copyTo(blurredBitmap);

        return blurredBitmap;

    }
}
