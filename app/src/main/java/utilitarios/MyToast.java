package utilitarios;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.sucen.sisamobii.R;

public class MyToast {
    private final Context context;
    private final int duration;
    private Runnable dismissRunnable;
    private Dialog dialog;
    private Handler handler;

    public MyToast(Context cont, int duration) {
        this.context = cont;
        this.duration = duration;
    }

    public void show(CharSequence text) {
        // Validação de segurança: Não exibe janela se a Activity estiver finalizada ou nula
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isFinishing()) {
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
                return;
            }
        }

        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (li == null) return;

        View v = li.inflate(R.layout.custom_toast, null);

        TextView tv = v.findViewById(R.id.text_toast);
        tv.setText(text);

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(v);

        Window window = dialog.getWindow();
        if (window != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setFlags(
                    android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            );

            WindowManager.LayoutParams params = window.getAttributes();

            // Define o alinhamento para a parte inferior
            params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

            // Define uma margem inferior em pixels para não colar na borda da tela
            // Exemplo: 100 pixels (você pode ajustar este valor)
            params.y = 100;

            window.setAttributes(params);
        }

        try {
            dialog.show();
        } catch (Exception e) {
            // Evita encerramento súbito do app se a janela falhar
            e.printStackTrace();
            return;
        }

        long delayMillis = (duration == Toast.LENGTH_LONG) ? 3500 : 2000;

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            try {
                if (dialog != null && dialog.isShowing()) {
                    if (context instanceof Activity) {
                        Activity activity = (Activity) context;
                        if (!activity.isFinishing()) {
                            dialog.dismiss();
                        }
                    } else {
                        dialog.dismiss();
                    }
                }
            } catch (Exception ignored) {
            }
        }, delayMillis);
    }

    public void cancel() {
        if (dismissRunnable != null && handler != null) {
            handler.removeCallbacks(dismissRunnable);
        }
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception ignored) {
            }
        }
    }
}