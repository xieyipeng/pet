package com.example.a13834598889.lovepets.Tool;

import android.content.Context;
import android.net.Uri;

/**
 * Created by 13834598889 on 2018/4/30.
 */

public class ResourceIdToUri {
    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    public static Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }
}
