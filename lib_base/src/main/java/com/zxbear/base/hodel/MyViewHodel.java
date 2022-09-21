package com.zxbear.base.hodel;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressWarnings("unchecked")
public class MyViewHodel {
    private View itemView;

    public MyViewHodel(View itemView) {
        this.itemView = itemView;
    }

    public LinearLayout getLinearLayout(int id) {
        return (LinearLayout) itemView.findViewById(id);
    }

    public RelativeLayout getRelativeLayout(int id) {
        return (RelativeLayout) itemView.findViewById(id);
    }

    public TextView getTextView(int id) {
        return (TextView) itemView.findViewById(id);
    }

    public EditText getEditText(int id) {
        return (EditText) itemView.findViewById(id);
    }

    public ImageView getImageView(int id) {
        return (ImageView) itemView.findViewById(id);
    }

    public ImageButton getImageButton(int id) {
        return (ImageButton) itemView.findViewById(id);
    }

    public Button getButton(int id) {
        return (Button) itemView.findViewById(id);
    }

    public CheckBox getCheckBox(int id) {
        return (CheckBox) itemView.findViewById(id);
    }

    public RadioButton getRadioButton(int id) {
        return (RadioButton) itemView.findViewById(id);
    }

    public GridLayout getGridLayout(int id) {
        return (GridLayout) itemView.findViewById(id);
    }
}
