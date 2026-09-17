package utilitarios;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import com.sucen.sisamobii.R;

public class AboutDialog extends AppCompatDialog {

    private final String title;
    private final CharSequence message;

    public AboutDialog(@NonNull Context context, String title, String message) {
        super(context);
        this.title = title;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_dialog);

        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            int width = (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.85);
            window.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvMessage = findViewById(R.id.tvMessage);
        Button btnClose = findViewById(R.id.btnClose);

        if (tvTitle != null) {
            tvTitle.setText(title);
        }

        if (tvMessage != null && message != null) {
            // Usa o Html.fromHtml() explicitamente para processar o HTML
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvMessage.setText(Html.fromHtml((String) message, Html.FROM_HTML_MODE_LEGACY));
                }
            } else {
                tvMessage.setText(Html.fromHtml((String) message));
            }
        }

        if (btnClose != null) {
            btnClose.setOnClickListener(v -> dismiss());
        }
    }
}