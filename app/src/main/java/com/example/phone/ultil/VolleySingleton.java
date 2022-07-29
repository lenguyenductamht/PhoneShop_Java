package com.example.phone.ultil;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

// nếu gọi n lần thì n RequestQueue dc tạ ra
//=> chung ta chỉ cần duy nhất 1 RequestQueue cho chạy toan app
public class VolleySingleton {
    private static final String TAG = "VolleySingleton";
    private static VolleySingleton sInstance;
    private RequestQueue mRequestQueue;

    private VolleySingleton(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (sInstance == null)
            sInstance = new VolleySingleton(context);
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

}
