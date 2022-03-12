package com.tungsten.hmclpe.launcher.dialogs.control;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.control.bean.BaseRockerViewInfo;
import com.tungsten.hmclpe.control.bean.rocker.RockerStyle;
import com.tungsten.hmclpe.control.view.BaseRockerView;
import com.tungsten.hmclpe.launcher.dialogs.tools.ColorSelectorDialog;
import com.tungsten.hmclpe.launcher.setting.SettingUtils;
import com.tungsten.hmclpe.utils.convert.ConvertUtils;

import java.util.ArrayList;

public class EditRockerDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    private String pattern;
    private String child;
    private int screenWidth;
    private int screenHeight;
    private BaseRockerView baseRockerView;

    private BaseRockerViewInfo baseRockerViewInfo;

    private Button positive;
    private Button negative;

    private ArrayAdapter<String> sizeTypeAdapter;
    private ArrayAdapter<String> positionTypeAdapter;
    private ArrayAdapter<String> sizeObjectAdapter;
    private ArrayAdapter<String> followTypeAdapter;
    private ArrayAdapter<String> rockerStyleAdapter;

    private Spinner rockerSizeTypeSpinner;
    private Spinner rockerPositionTypeSpinner;
    private LinearLayout sizeObjectLayout;
    private Spinner sizeObjectSpinner;
    private SeekBar rockerSizeSeekbar;
    private SeekBar rockerXSeekbar;
    private SeekBar rockerYSeekbar;
    private TextView rockerSizeText;
    private TextView rockerXText;
    private TextView rockerYText;
    private Spinner followTypeSpinner;
    private SwitchCompat checkShift;
    private Spinner selectExistRockerStyle;
    private SwitchCompat checkUsingExistRockerStyle;
    private Button createRockerStyle;
    private LinearLayout rockerStyleLayout;
    private SeekBar rockerCornerRadiusSeekbar;
    private SeekBar rockerCornerRadiusPressSeekbar;
    private SeekBar rockerStrokeWidthSeekbar;
    private SeekBar rockerStrokeWidthPressSeekbar;
    private TextView rockerCornerRadiusText;
    private TextView rockerStrokeWidthText;
    private TextView rockerCornerRadiusPressedText;
    private TextView rockerStrokeWidthPressedText;
    private Button selectPointerColor;
    private Button selectRockerStrokeColor;
    private Button selectRockerFillColor;
    private Button selectPointerColorPressed;
    private Button selectRockerStrokeColorPressed;
    private Button selectRockerFillColorPressed;
    private View pointerColorPre;
    private View rockerStrokeColorPre;
    private View rockerFillColorPre;
    private View pointerColorPressedPre;
    private View rockerStrokeColorPressedPre;
    private View rockerFillColorPressedPre;
    private TextView pointerColorText;
    private TextView rockerStrokeColorText;
    private TextView rockerFillColorText;
    private TextView pointerColorPressedText;
    private TextView rockerStrokeColorPressedText;
    private TextView rockerFillColorPressedText;

    private RockerStyle selectedRockerStyle;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public EditRockerDialog(@NonNull Context context, String pattern, String child, int screenWidth, int screenHeight, BaseRockerView baseRockerView, boolean fullscreen) {
        super(context);
        this.pattern = pattern;
        this.child = child;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.baseRockerView = baseRockerView;
        setContentView(R.layout.dialog_edit_rocker);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (fullscreen) {
                getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            } else {
                getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
            }
        }
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init(){
        baseRockerViewInfo = baseRockerView.info;

        positive = findViewById(R.id.apply_rocker_change);
        negative = findViewById(R.id.exit);

        positive.setOnClickListener(this);
        negative.setOnClickListener(this);

        ArrayList<String> sizeType = new ArrayList<>();
        ArrayList<String> positionType = new ArrayList<>();
        ArrayList<String> sizeObject = new ArrayList<>();
        ArrayList<String> followType = new ArrayList<>();
        sizeType.add(getContext().getString(R.string.dialog_add_view_size_type_percent));
        sizeType.add(getContext().getString(R.string.dialog_add_view_size_type_absolute));
        positionType.add(getContext().getString(R.string.dialog_add_view_position_type_percent));
        positionType.add(getContext().getString(R.string.dialog_add_view_position_type_absolute));
        sizeObject.add(getContext().getString(R.string.dialog_add_view_size_type_percent_object_width));
        sizeObject.add(getContext().getString(R.string.dialog_add_view_size_type_percent_object_height));
        followType.add(getContext().getString(R.string.dialog_add_view_rocker_function_follow_none));
        followType.add(getContext().getString(R.string.dialog_add_view_rocker_function_follow_part));
        followType.add(getContext().getString(R.string.dialog_add_view_rocker_function_follow_all));
        sizeTypeAdapter = new ArrayAdapter<>(getContext(),R.layout.item_spinner, sizeType);
        positionTypeAdapter = new ArrayAdapter<>(getContext(),R.layout.item_spinner, positionType);
        sizeObjectAdapter = new ArrayAdapter<>(getContext(),R.layout.item_spinner, sizeObject);
        followTypeAdapter = new ArrayAdapter<>(getContext(),R.layout.item_spinner, followType);

        initRockerLayout();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private void initRockerLayout(){
        rockerSizeTypeSpinner = findViewById(R.id.rocker_size_type);
        rockerPositionTypeSpinner = findViewById(R.id.rocker_position_type);
        sizeObjectLayout = findViewById(R.id.size_object_layout);
        sizeObjectSpinner = findViewById(R.id.size_object);
        rockerSizeSeekbar = findViewById(R.id.rocker_size_seekbar);
        rockerXSeekbar = findViewById(R.id.rocker_x_seekbar);
        rockerYSeekbar = findViewById(R.id.rocker_y_seekbar);
        rockerSizeText = findViewById(R.id.size_text);
        rockerXText = findViewById(R.id.rocker_x_text);
        rockerYText = findViewById(R.id.rocker_y_text);
        followTypeSpinner = findViewById(R.id.function_follow);
        checkShift = findViewById(R.id.function_shift);
        selectExistRockerStyle = findViewById(R.id.exterior_use_exist_rocker);
        checkUsingExistRockerStyle = findViewById(R.id.switch_exterior_use_exist_rocker);
        createRockerStyle = findViewById(R.id.exterior_add_rocker_style);
        rockerStyleLayout = findViewById(R.id.rocker_style_layout);
        rockerCornerRadiusSeekbar = findViewById(R.id.rocker_exterior_corner_radius_seekbar);
        rockerCornerRadiusPressSeekbar = findViewById(R.id.rocker_exterior_corner_radius_seekbar_pressed);
        rockerStrokeWidthSeekbar = findViewById(R.id.rocker_exterior_stroke_width_seekbar);
        rockerStrokeWidthPressSeekbar = findViewById(R.id.rocker_exterior_stroke_width_seekbar_pressed);
        rockerCornerRadiusText = findViewById(R.id.rocker_corner_radius_text);
        rockerCornerRadiusPressedText = findViewById(R.id.rocker_corner_radius_press_text);
        rockerStrokeWidthText = findViewById(R.id.rocker_stroke_width_text);
        rockerStrokeWidthPressedText = findViewById(R.id.rocker_stroke_width_press_text);
        selectPointerColor = findViewById(R.id.exterior_pointer_color);
        selectRockerStrokeColor = findViewById(R.id.rocker_exterior_stroke_color);
        selectRockerFillColor = findViewById(R.id.rocker_exterior_fill_color);
        selectPointerColorPressed = findViewById(R.id.exterior_pointer_color_pressed);
        selectRockerStrokeColorPressed = findViewById(R.id.rocker_exterior_stroke_color_pressed);
        selectRockerFillColorPressed = findViewById(R.id.rocker_exterior_fill_color_pressed);
        pointerColorPre = findViewById(R.id.pointer_color_preview);
        rockerStrokeColorPre = findViewById(R.id.rocker_stroke_color_preview);
        rockerFillColorPre = findViewById(R.id.rocker_fill_color_preview);
        pointerColorPressedPre = findViewById(R.id.pointer_pressed_color_preview);
        rockerStrokeColorPressedPre = findViewById(R.id.rocker_stroke_pressed_color_preview);
        rockerFillColorPressedPre = findViewById(R.id.rocker_fill_pressed_color_preview);
        pointerColorText = findViewById(R.id.pointer_color_text);
        rockerStrokeColorText = findViewById(R.id.rocker_stroke_color_text);
        rockerFillColorText = findViewById(R.id.rocker_fill_color_text);
        pointerColorPressedText = findViewById(R.id.pointer_pressed_color_text);
        rockerStrokeColorPressedText = findViewById(R.id.rocker_stroke_pressed_color_text);
        rockerFillColorPressedText = findViewById(R.id.rocker_fill_pressed_color_text);

        rockerSizeTypeSpinner.setAdapter(sizeTypeAdapter);
        rockerSizeTypeSpinner.setSelection(baseRockerViewInfo.sizeType);
        rockerPositionTypeSpinner.setAdapter(positionTypeAdapter);
        rockerPositionTypeSpinner.setSelection(baseRockerViewInfo.positionType);
        if (baseRockerViewInfo.sizeType == BaseRockerViewInfo.SIZE_TYPE_ABSOLUTE) {
            sizeObjectLayout.setVisibility(View.GONE);
        }
        sizeObjectSpinner.setAdapter(sizeObjectAdapter);
        sizeObjectSpinner.setSelection(baseRockerViewInfo.size.object);
        followTypeSpinner.setAdapter(followTypeAdapter);
        followTypeSpinner.setSelection(baseRockerViewInfo.followType);
        if (baseRockerViewInfo.sizeType == BaseRockerViewInfo.SIZE_TYPE_PERCENT) {
            rockerSizeSeekbar.setMin(1);
            rockerSizeSeekbar.setMax(1000);
            rockerSizeSeekbar.setProgress((int) (1000 * baseRockerViewInfo.size.percentSize));
            rockerSizeText.setText(((int) (100 * baseRockerViewInfo.size.percentSize)) / 10f + " %");
        }
        else {
            rockerSizeSeekbar.setMin(1);
            rockerSizeSeekbar.setMax(300);
            rockerSizeSeekbar.setProgress(baseRockerViewInfo.size.absoluteSize);
            rockerSizeText.setText(baseRockerViewInfo.size.absoluteSize + " dp");
        }
        if (baseRockerViewInfo.positionType == BaseRockerViewInfo.POSITION_TYPE_PERCENT) {
            rockerXSeekbar.setMax(1000);
            rockerYSeekbar.setMax(1000);
            rockerXSeekbar.setProgress((int) (1000 * baseRockerViewInfo.xPosition.percentPosition));
            rockerYSeekbar.setProgress((int) (1000 * baseRockerViewInfo.yPosition.percentPosition));
            rockerXText.setText(((int) (1000 * baseRockerViewInfo.xPosition.percentPosition)) / 10f + " %");
            rockerYText.setText(((int) (1000 * baseRockerViewInfo.yPosition.percentPosition)) / 10f + " %");
        }
        else {
            rockerXSeekbar.setMax(ConvertUtils.px2dip(getContext(),screenWidth));
            rockerYSeekbar.setMax(ConvertUtils.px2dip(getContext(),screenHeight));
            rockerXSeekbar.setProgress(baseRockerViewInfo.xPosition.absolutePosition);
            rockerYSeekbar.setProgress(baseRockerViewInfo.yPosition.absolutePosition);
            rockerXText.setText(baseRockerViewInfo.xPosition.absolutePosition + " dp");
            rockerYText.setText(baseRockerViewInfo.yPosition.absolutePosition + " dp");
        }
        onRockerSizeChange();
        checkShift.setChecked(baseRockerViewInfo.shift);
        refreshRockerStyleList(true);
        ArrayList<String> names = new ArrayList<>();
        for (RockerStyle style : SettingUtils.getRockerStyleList()){
            names.add(style.name);
        }
        if (baseRockerViewInfo.usingExist && names.contains(baseRockerViewInfo.rockerStyle.name)) {
            rockerStyleLayout.setVisibility(View.GONE);
            checkUsingExistRockerStyle.setChecked(true);
            selectExistRockerStyle.setSelection(rockerStyleAdapter.getPosition(baseRockerViewInfo.rockerStyle.name));
            selectedRockerStyle = baseRockerViewInfo.rockerStyle;
        }
        else {
            baseRockerViewInfo.usingExist = false;
            baseRockerViewInfo.rockerStyle.name = "";
            selectedRockerStyle = SettingUtils.getRockerStyleList().get(0);
        }
        refreshRockerStyleEditor();

        rockerSizeTypeSpinner.setOnItemSelectedListener(this);
        rockerPositionTypeSpinner.setOnItemSelectedListener(this);
        sizeObjectSpinner.setOnItemSelectedListener(this);
        followTypeSpinner.setOnItemSelectedListener(this);
        selectExistRockerStyle.setOnItemSelectedListener(this);
        rockerSizeSeekbar.setOnSeekBarChangeListener(this);
        rockerXSeekbar.setOnSeekBarChangeListener(this);
        rockerYSeekbar.setOnSeekBarChangeListener(this);
        rockerCornerRadiusSeekbar.setOnSeekBarChangeListener(this);
        rockerCornerRadiusPressSeekbar.setOnSeekBarChangeListener(this);
        rockerStrokeWidthSeekbar.setOnSeekBarChangeListener(this);
        rockerStrokeWidthPressSeekbar.setOnSeekBarChangeListener(this);
        checkShift.setOnCheckedChangeListener(this);
        checkUsingExistRockerStyle.setOnCheckedChangeListener(this);
        createRockerStyle.setOnClickListener(this);
        selectPointerColor.setOnClickListener(this);
        selectPointerColorPressed.setOnClickListener(this);
        selectRockerFillColor.setOnClickListener(this);
        selectRockerFillColorPressed.setOnClickListener(this);
        selectRockerStrokeColor.setOnClickListener(this);
        selectRockerStrokeColorPressed.setOnClickListener(this);
    }

    public void refreshRockerStyleList(boolean first){
        ArrayList<RockerStyle> styles = SettingUtils.getRockerStyleList();
        ArrayList<String> names = new ArrayList<>();
        for (RockerStyle style : styles){
            names.add(style.name);
        }
        rockerStyleAdapter = new ArrayAdapter<>(getContext(),R.layout.item_spinner,names);
        selectExistRockerStyle.setAdapter(rockerStyleAdapter);
        if (!first) {
            if (baseRockerViewInfo.usingExist && names.contains(baseRockerViewInfo.rockerStyle.name)) {
                rockerStyleLayout.setVisibility(View.GONE);
                checkUsingExistRockerStyle.setChecked(true);
                selectExistRockerStyle.setSelection(rockerStyleAdapter.getPosition(baseRockerViewInfo.rockerStyle.name));
                selectedRockerStyle = baseRockerViewInfo.rockerStyle;
            }
            else {
                rockerStyleLayout.setVisibility(View.VISIBLE);
                checkUsingExistRockerStyle.setChecked(false);
                baseRockerViewInfo.usingExist = false;
                baseRockerViewInfo.rockerStyle.name = "";
                selectedRockerStyle = SettingUtils.getRockerStyleList().get(0);
            }
            refreshRockerStyleEditor();
        }
    }

    @SuppressLint("SetTextI18n")
    private void onRockerSizeChange(){
        int xMax;
        int yMax;
        if (baseRockerViewInfo.sizeType == BaseRockerViewInfo.SIZE_TYPE_PERCENT) {
            int sizeObject = baseRockerViewInfo.size.object == BaseRockerViewInfo.SIZE_OBJECT_WIDTH ? screenWidth : screenHeight;
            xMax = (int) (ConvertUtils.px2dip(getContext(),screenWidth) - (ConvertUtils.px2dip(getContext(),sizeObject) * baseRockerViewInfo.size.percentSize));
            yMax = (int) (ConvertUtils.px2dip(getContext(),screenHeight) - (ConvertUtils.px2dip(getContext(),sizeObject) * baseRockerViewInfo.size.percentSize));
        }
        else {
            xMax = ConvertUtils.px2dip(getContext(),screenWidth) - baseRockerViewInfo.size.absoluteSize;
            yMax = ConvertUtils.px2dip(getContext(),screenHeight) - baseRockerViewInfo.size.absoluteSize;
        }
        if (baseRockerViewInfo.positionType == BaseRockerViewInfo.POSITION_TYPE_ABSOLUTE){
            if (baseRockerViewInfo.xPosition.absolutePosition > xMax){
                baseRockerViewInfo.xPosition.absolutePosition = xMax;
                rockerXSeekbar.setProgress(xMax);
                rockerXText.setText(xMax + " dp");
            }
            if (baseRockerViewInfo.yPosition.absolutePosition > yMax){
                baseRockerViewInfo.yPosition.absolutePosition = yMax;
                rockerYSeekbar.setProgress(yMax);
                rockerYText.setText(yMax + " dp");
            }
            rockerXSeekbar.setMax(xMax);
            rockerYSeekbar.setMax(yMax);
        }
        else {
            if (baseRockerViewInfo.xPosition.absolutePosition > xMax){
                baseRockerViewInfo.xPosition.absolutePosition = xMax;
            }
            if (baseRockerViewInfo.yPosition.absolutePosition > yMax){
                baseRockerViewInfo.yPosition.absolutePosition = yMax;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void refreshRockerStyleEditor(){
        rockerCornerRadiusSeekbar.setProgress(baseRockerViewInfo.rockerStyle.cornerRadius);
        rockerStrokeWidthSeekbar.setProgress((int) (baseRockerViewInfo.rockerStyle.strokeWidth * 10));
        rockerCornerRadiusPressSeekbar.setProgress(baseRockerViewInfo.rockerStyle.cornerRadiusPress);
        rockerStrokeWidthPressSeekbar.setProgress((int) (baseRockerViewInfo.rockerStyle.strokeWidthPress * 10));
        rockerCornerRadiusText.setText(baseRockerViewInfo.rockerStyle.cornerRadius + " dp");
        rockerStrokeWidthText.setText(baseRockerViewInfo.rockerStyle.strokeWidth + " dp");
        rockerCornerRadiusPressedText.setText(baseRockerViewInfo.rockerStyle.cornerRadiusPress + " dp");
        rockerStrokeWidthPressedText.setText(baseRockerViewInfo.rockerStyle.strokeWidthPress + " dp");
        pointerColorPre.setBackgroundColor(Color.parseColor(baseRockerViewInfo.rockerStyle.pointerColor));
        rockerStrokeColorPre.setBackgroundColor(Color.parseColor(baseRockerViewInfo.rockerStyle.strokeColor));
        rockerFillColorPre.setBackgroundColor(Color.parseColor(baseRockerViewInfo.rockerStyle.fillColor));
        pointerColorPressedPre.setBackgroundColor(Color.parseColor(baseRockerViewInfo.rockerStyle.pointerColorPress));
        rockerStrokeColorPressedPre.setBackgroundColor(Color.parseColor(baseRockerViewInfo.rockerStyle.strokeColorPress));
        rockerFillColorPressedPre.setBackgroundColor(Color.parseColor(baseRockerViewInfo.rockerStyle.fillColorPress));
        pointerColorText.setText(baseRockerViewInfo.rockerStyle.pointerColor);
        rockerStrokeColorText.setText(baseRockerViewInfo.rockerStyle.strokeColor);
        rockerFillColorText.setText(baseRockerViewInfo.rockerStyle.fillColor);
        pointerColorPressedText.setText(baseRockerViewInfo.rockerStyle.pointerColorPress);
        rockerStrokeColorPressedText.setText(baseRockerViewInfo.rockerStyle.strokeColorPress);
        rockerFillColorPressedText.setText(baseRockerViewInfo.rockerStyle.fillColorPress);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        if (view == positive){
            baseRockerView.info.refresh(baseRockerViewInfo);
            baseRockerView.updateSizeAndPosition(baseRockerViewInfo);
            baseRockerView.refreshInfo(baseRockerViewInfo);
            baseRockerView.saveRockerInfo();
            dismiss();
        }
        if (view == negative){
            dismiss();
        }

        if (view == createRockerStyle) {
            RockerStyleManagerDialog dialog = new RockerStyleManagerDialog(getContext(), () -> {
                refreshRockerStyleList(false);
            });
            dialog.show();
        }
        if (view == selectPointerColor) {
            ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getContext(),false,Color.parseColor(baseRockerViewInfo.rockerStyle.pointerColor));
            colorSelectorDialog.setColorSelectorDialogListener(new ColorSelectorDialog.ColorSelectorDialogListener() {
                @Override
                public void onColorSelected(int color) {

                }

                @Override
                public void onPositive(int destColor) {
                    pointerColorPre.setBackgroundColor(destColor);
                    pointerColorText.setText("#" + Integer.toHexString(destColor));
                    baseRockerViewInfo.rockerStyle.pointerColor = "#" + Integer.toHexString(destColor);
                }

                @Override
                public void onNegative(int initColor) {

                }
            });
            colorSelectorDialog.show();
        }
        if (view == selectRockerStrokeColor) {
            ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getContext(),false,Color.parseColor(baseRockerViewInfo.rockerStyle.strokeColor));
            colorSelectorDialog.setColorSelectorDialogListener(new ColorSelectorDialog.ColorSelectorDialogListener() {
                @Override
                public void onColorSelected(int color) {

                }

                @Override
                public void onPositive(int destColor) {
                    rockerStrokeColorPre.setBackgroundColor(destColor);
                    rockerStrokeColorText.setText("#" + Integer.toHexString(destColor));
                    baseRockerViewInfo.rockerStyle.strokeColor = "#" + Integer.toHexString(destColor);
                }

                @Override
                public void onNegative(int initColor) {

                }
            });
            colorSelectorDialog.show();
        }
        if (view == selectRockerFillColor) {
            ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getContext(),false,Color.parseColor(baseRockerViewInfo.rockerStyle.fillColor));
            colorSelectorDialog.setColorSelectorDialogListener(new ColorSelectorDialog.ColorSelectorDialogListener() {
                @Override
                public void onColorSelected(int color) {

                }

                @Override
                public void onPositive(int destColor) {
                    rockerFillColorPre.setBackgroundColor(destColor);
                    rockerFillColorText.setText("#" + Integer.toHexString(destColor));
                    baseRockerViewInfo.rockerStyle.fillColor = "#" + Integer.toHexString(destColor);
                }

                @Override
                public void onNegative(int initColor) {

                }
            });
            colorSelectorDialog.show();
        }
        if (view == selectPointerColorPressed) {
            ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getContext(),false,Color.parseColor(baseRockerViewInfo.rockerStyle.pointerColorPress));
            colorSelectorDialog.setColorSelectorDialogListener(new ColorSelectorDialog.ColorSelectorDialogListener() {
                @Override
                public void onColorSelected(int color) {

                }

                @Override
                public void onPositive(int destColor) {
                    pointerColorPressedPre.setBackgroundColor(destColor);
                    pointerColorPressedText.setText("#" + Integer.toHexString(destColor));
                    baseRockerViewInfo.rockerStyle.pointerColorPress = "#" + Integer.toHexString(destColor);
                }

                @Override
                public void onNegative(int initColor) {

                }
            });
            colorSelectorDialog.show();
        }
        if (view == selectRockerStrokeColorPressed) {
            ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getContext(),false,Color.parseColor(baseRockerViewInfo.rockerStyle.strokeColorPress));
            colorSelectorDialog.setColorSelectorDialogListener(new ColorSelectorDialog.ColorSelectorDialogListener() {
                @Override
                public void onColorSelected(int color) {

                }

                @Override
                public void onPositive(int destColor) {
                    rockerStrokeColorPressedPre.setBackgroundColor(destColor);
                    rockerStrokeColorPressedText.setText("#" + Integer.toHexString(destColor));
                    baseRockerViewInfo.rockerStyle.strokeColorPress = "#" + Integer.toHexString(destColor);
                }

                @Override
                public void onNegative(int initColor) {

                }
            });
            colorSelectorDialog.show();
        }
        if (view == selectRockerFillColorPressed) {
            ColorSelectorDialog colorSelectorDialog = new ColorSelectorDialog(getContext(),false,Color.parseColor(baseRockerViewInfo.rockerStyle.fillColorPress));
            colorSelectorDialog.setColorSelectorDialogListener(new ColorSelectorDialog.ColorSelectorDialogListener() {
                @Override
                public void onColorSelected(int color) {

                }

                @Override
                public void onPositive(int destColor) {
                    rockerFillColorPressedPre.setBackgroundColor(destColor);
                    rockerFillColorPressedText.setText("#" + Integer.toHexString(destColor));
                    baseRockerViewInfo.rockerStyle.fillColorPress = "#" + Integer.toHexString(destColor);
                }

                @Override
                public void onNegative(int initColor) {

                }
            });
            colorSelectorDialog.show();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == rockerSizeTypeSpinner) {
            baseRockerViewInfo.sizeType = i;
            if (i == BaseRockerViewInfo.SIZE_TYPE_PERCENT) {
                sizeObjectLayout.setVisibility(View.VISIBLE);
                rockerSizeSeekbar.setMax(1000);
                rockerSizeSeekbar.setProgress((int) (baseRockerViewInfo.size.percentSize * 1000));
                rockerSizeText.setText(((int) (1000 * baseRockerViewInfo.size.percentSize)) / 10f + " %");
            }
            else {
                sizeObjectLayout.setVisibility(View.GONE);
                rockerSizeSeekbar.setMax(300);
                rockerSizeSeekbar.setProgress(baseRockerViewInfo.size.absoluteSize);
                rockerSizeText.setText(baseRockerViewInfo.size.absoluteSize + " dp");
            }
            onRockerSizeChange();
        }
        if (adapterView == rockerPositionTypeSpinner) {
            baseRockerViewInfo.positionType = i;
            if (i == BaseRockerViewInfo.POSITION_TYPE_PERCENT) {
                rockerXSeekbar.setMax(1000);
                rockerYSeekbar.setMax(1000);
                rockerXSeekbar.setProgress((int) (baseRockerViewInfo.xPosition.percentPosition * 1000));
                rockerYSeekbar.setProgress((int) (baseRockerViewInfo.yPosition.percentPosition * 1000));
                rockerXText.setText(((int) (1000 * baseRockerViewInfo.xPosition.percentPosition)) / 10f + " %");
                rockerYText.setText(((int) (1000 * baseRockerViewInfo.yPosition.percentPosition)) / 10f + " %");
            }
            else {
                rockerXSeekbar.setMax(ConvertUtils.px2dip(getContext(),screenWidth));
                rockerYSeekbar.setMax(ConvertUtils.px2dip(getContext(),screenHeight));
                rockerXSeekbar.setProgress(baseRockerViewInfo.xPosition.absolutePosition);
                rockerYSeekbar.setProgress(baseRockerViewInfo.yPosition.absolutePosition);
                rockerXText.setText(baseRockerViewInfo.xPosition.absolutePosition + " dp");
                rockerYText.setText(baseRockerViewInfo.yPosition.absolutePosition + " dp");
            }
            onRockerSizeChange();
        }
        if (adapterView == sizeObjectSpinner) {
            baseRockerViewInfo.size.object = i;
            onRockerSizeChange();
        }
        if (adapterView == followTypeSpinner) {
            baseRockerViewInfo.followType = i;
        }
        if (adapterView == selectExistRockerStyle) {
            for (RockerStyle style : SettingUtils.getRockerStyleList()){
                if (style.name.equals(selectExistRockerStyle.getItemAtPosition(i))){
                    selectedRockerStyle = style;
                }
            }
            if (baseRockerViewInfo.usingExist){
                baseRockerViewInfo.rockerStyle = selectedRockerStyle;
            }
            refreshRockerStyleEditor();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == checkShift) {
            baseRockerViewInfo.shift = b;
        }
        if (compoundButton == checkUsingExistRockerStyle) {
            baseRockerViewInfo.usingExist = b;
            if (b) {
                rockerStyleLayout.setVisibility(View.GONE);
                baseRockerViewInfo.rockerStyle = selectedRockerStyle;
            }
            else {
                rockerStyleLayout.setVisibility(View.VISIBLE);
                baseRockerViewInfo.rockerStyle.name = "";
            }
            refreshRockerStyleEditor();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (seekBar == rockerSizeSeekbar && b) {
            if (baseRockerViewInfo.sizeType == BaseRockerViewInfo.SIZE_TYPE_PERCENT) {
                baseRockerViewInfo.size.percentSize = ((float) i / 1000f);
                rockerSizeText.setText(((int) (1000 * baseRockerViewInfo.size.percentSize)) / 10f + " %");
            }
            else {
                baseRockerViewInfo.size.absoluteSize = i;
                rockerSizeText.setText(i + " dp");
            }
            onRockerSizeChange();
        }
        if (seekBar == rockerXSeekbar && b) {
            if (baseRockerViewInfo.positionType == BaseRockerViewInfo.POSITION_TYPE_PERCENT) {
                baseRockerViewInfo.xPosition.percentPosition = ((float) i / 1000f);
                rockerXText.setText(((int) (1000 * baseRockerViewInfo.xPosition.percentPosition)) / 10f + " %");
            }
            else {
                baseRockerViewInfo.xPosition.absolutePosition = i;
                rockerXText.setText(i + " dp");
            }
        }
        if (seekBar == rockerYSeekbar && b) {
            if (baseRockerViewInfo.positionType == BaseRockerViewInfo.POSITION_TYPE_PERCENT) {
                baseRockerViewInfo.yPosition.percentPosition = ((float) i / 1000f);
                rockerYText.setText(((int) (1000 * baseRockerViewInfo.yPosition.percentPosition)) / 10f + " %");
            }
            else {
                baseRockerViewInfo.yPosition.absolutePosition = i;
                rockerYText.setText(i + " dp");
            }
        }
        if (seekBar == rockerCornerRadiusSeekbar) {
            rockerCornerRadiusText.setText(i + " dp");
            baseRockerViewInfo.rockerStyle.cornerRadius = i;
        }
        if (seekBar == rockerStrokeWidthSeekbar) {
            rockerStrokeWidthText.setText((float) i / 10f + " dp");
            baseRockerViewInfo.rockerStyle.strokeWidth = (float) i / 10f;
        }
        if (seekBar == rockerCornerRadiusPressSeekbar) {
            rockerCornerRadiusPressedText.setText(i + " dp");
            baseRockerViewInfo.rockerStyle.cornerRadiusPress = i;
        }
        if (seekBar == rockerStrokeWidthPressSeekbar) {
            rockerStrokeWidthPressedText.setText((float) i / 10f + " dp");
            baseRockerViewInfo.rockerStyle.strokeWidthPress = (float) i / 10f;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
