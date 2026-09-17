package utilitarios;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import com.sucen.sisamobii.R;

public class LoadingDialog extends AppCompatDialog {

    private TextView tvMessage;
    private String message;

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    public LoadingDialog(@NonNull Context context, String message) {
        super(context);
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_dialog);

        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        tvMessage = findViewById(R.id.tvMessage);
        if (message != null && tvMessage != null) {
            tvMessage.setText(message);
        }
    }

    public void setMessage(String message) {
        this.message = message;
        if (tvMessage != null) {
            tvMessage.setText(message);
        }
    }
}