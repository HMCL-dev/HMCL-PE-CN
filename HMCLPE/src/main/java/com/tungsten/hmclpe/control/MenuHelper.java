package com.tungsten.hmclpe.control;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.control.view.LayoutPanel;
import com.tungsten.hmclpe.control.view.MenuFloat;
import com.tungsten.hmclpe.control.view.MenuView;
import com.tungsten.hmclpe.launcher.dialogs.control.ChildManagerDialog;
import com.tungsten.hmclpe.launcher.dialogs.control.EditControlPatternDialog;
import com.tungsten.hmclpe.launcher.list.local.controller.ChildLayout;
import com.tungsten.hmclpe.launcher.list.local.controller.ControlPattern;
import com.tungsten.hmclpe.launcher.setting.SettingUtils;
import com.tungsten.hmclpe.launcher.setting.game.GameMenuSetting;

import java.util.ArrayList;

public class MenuHelper implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    public Context context;
    public AppCompatActivity activity;
    public DrawerLayout drawerLayout;
    public LayoutPanel baseLayout;

    public GameMenuSetting gameMenuSetting;

    public SwitchCompat switchMenuFloat;
    public SwitchCompat switchMenuView;
    public SwitchCompat switchMenuSlide;

    public MenuFloat menuFloat;
    public MenuView menuView;

    public Spinner patternSpinner;
    public SwitchCompat editModeSwitch;
    public Button editInfo;
    public Button manageChild;
    public Spinner childSpinner;
    public Button addView;

    public ArrayList<ControlPattern> patternList;
    public ControlPattern currentPattern;
    public String currentChild;
    public boolean editMode;
    public ArrayList<String> childLayoutList;
    public ArrayAdapter<String> childAdapter;

    public MenuHelper(Context context, AppCompatActivity activity, DrawerLayout drawerLayout, LayoutPanel baseLayout,boolean editMode,String currentPattern){
        this.context = context;
        this.activity = activity;
        this.drawerLayout = drawerLayout;
        this.baseLayout = baseLayout;
        this.editMode = editMode;
        patternList = SettingUtils.getControlPatternList();
        for (ControlPattern controlPattern : patternList){
            if (controlPattern.name.equals(currentPattern)){
                this.currentPattern = controlPattern;
            }
        }

        gameMenuSetting = GameMenuSetting.getGameMenuSetting();
        init();
    }

    public void init(){
        switchMenuFloat = activity.findViewById(R.id.switch_float_button);
        switchMenuView = activity.findViewById(R.id.switch_bar);
        switchMenuSlide = activity.findViewById(R.id.switch_gesture);

        switchMenuFloat.setChecked(gameMenuSetting.menuFloatSetting.enable);
        switchMenuView.setChecked(gameMenuSetting.menuViewSetting.enable);
        switchMenuSlide.setChecked(gameMenuSetting.menuSlideSetting);

        switchMenuFloat.setOnCheckedChangeListener(this);
        switchMenuView.setOnCheckedChangeListener(this);
        switchMenuSlide.setOnCheckedChangeListener(this);

        patternSpinner = activity.findViewById(R.id.current_pattern_spinner);
        editModeSwitch = activity.findViewById(R.id.switch_edit_mode);
        editInfo = activity.findViewById(R.id.edit_pattern_info);
        manageChild = activity.findViewById(R.id.manage_child_layout);
        childSpinner = activity.findViewById(R.id.current_child_spinner);
        addView = activity.findViewById(R.id.add_view);

        ArrayList<String> patterns = new ArrayList<>();
        for (ControlPattern controlPattern : patternList){
            patterns.add(controlPattern.name);
        }
        ArrayAdapter<String> patternAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,patterns);
        patternSpinner.setAdapter(patternAdapter);
        patternSpinner.setSelection(patternAdapter.getPosition(currentPattern.name));

        ArrayList<ChildLayout> list = SettingUtils.getChildList(currentPattern.name);
        childLayoutList = new ArrayList<>();
        for (ChildLayout childLayout : list){
            childLayoutList.add(childLayout.name);
        }
        childAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,childLayoutList);
        childSpinner.setAdapter(childAdapter);

        patternSpinner.setOnItemSelectedListener(this);
        editModeSwitch.setOnCheckedChangeListener(this);
        editInfo.setOnClickListener(this);
        manageChild.setOnClickListener(this);
        childSpinner.setOnItemSelectedListener(this);
        addView.setOnClickListener(this);

        childSpinner.setSelection(0);

        baseLayout.post(new Runnable() {
            @Override
            public void run() {
                menuFloat = new MenuFloat(context,baseLayout.getWidth(),baseLayout.getHeight(),gameMenuSetting.menuFloatSetting.positionX,gameMenuSetting.menuFloatSetting.positionY);
                menuFloat.addCallback(new MenuFloat.MenuFloatCallback() {
                    @Override
                    public void onClick() {
                        drawerLayout.openDrawer(GravityCompat.END,true);
                        drawerLayout.openDrawer(GravityCompat.START,true);
                    }

                    @Override
                    public void onMove(float xPosition, float yPosition) {
                        gameMenuSetting.menuFloatSetting.positionX = xPosition;
                        gameMenuSetting.menuFloatSetting.positionY = yPosition;
                        GameMenuSetting.saveGameMenuSetting(gameMenuSetting);
                    }
                });
                menuView = new MenuView(context,baseLayout.getWidth(),baseLayout.getHeight(),gameMenuSetting.menuViewSetting.mode,gameMenuSetting.menuViewSetting.yPercent);
                menuView.addCallback(new MenuView.MenuCallback() {
                    @Override
                    public void onRelease() {
                        drawerLayout.openDrawer(GravityCompat.END,true);
                        drawerLayout.openDrawer(GravityCompat.START,true);
                    }

                    @Override
                    public void onMoveModeStart() {

                    }

                    @Override
                    public void onMove(int mode, float yPercent) {
                        gameMenuSetting.menuViewSetting.mode = mode;
                        gameMenuSetting.menuViewSetting.yPercent = yPercent;
                        GameMenuSetting.saveGameMenuSetting(gameMenuSetting);
                    }

                    @Override
                    public void onMoveModeStop() {

                    }
                });
                if (gameMenuSetting.menuFloatSetting.enable){
                    baseLayout.addView(menuFloat);
                }
                if (gameMenuSetting.menuViewSetting.enable){
                    baseLayout.addView(menuView);
                }
                checkOpenMenuSetting();
            }
        });

        if (gameMenuSetting.menuSlideSetting){
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    private void checkOpenMenuSetting(){
        if (!gameMenuSetting.menuFloatSetting.enable && !gameMenuSetting.menuViewSetting.enable && !gameMenuSetting.menuSlideSetting){
            switchMenuFloat.setChecked(true);
        }
    }

    public void refreshChildSpinner(){

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == switchMenuFloat){
            gameMenuSetting.menuFloatSetting.enable = b;
            if (b){
                baseLayout.addView(menuFloat);
            }
            else {
                baseLayout.removeView(menuFloat);
            }
            checkOpenMenuSetting();
        }
        if (compoundButton == switchMenuView){
            gameMenuSetting.menuViewSetting.enable = b;
            if (b){
                baseLayout.addView(menuView);
            }
            else {
                baseLayout.removeView(menuView);
            }
            checkOpenMenuSetting();
        }
        if (compoundButton == switchMenuSlide){
            gameMenuSetting.menuSlideSetting = b;
            if (b){
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
            else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
            checkOpenMenuSetting();
        }
        GameMenuSetting.saveGameMenuSetting(gameMenuSetting);
    }

    @Override
    public void onClick(View view) {
        if (view == editInfo){
            EditControlPatternDialog dialog = new EditControlPatternDialog(context, new EditControlPatternDialog.OnPatternInfoChangeListener() {
                @Override
                public void OnInfoChange(ControlPattern controlPattern) {

                }
            },currentPattern);
            dialog.show();
        }
        if (view == manageChild){
            ChildManagerDialog dialog = new ChildManagerDialog(context,this,currentPattern);
            dialog.show();
        }
        if (view == addView){
            if (currentChild == null){
                Toast.makeText(context,context.getString(R.string.drawer_custom_menu_warn),Toast.LENGTH_SHORT).show();
            }
            else {

            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
