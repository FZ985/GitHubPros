package com.kmt.pro.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.kmt.pro.R;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

/**
 * Create by JFZ
 * date: 2020-07-16 16:57
 **/
public class IPasswordView2 extends AppCompatEditText {
    //单个密码大小
    private int maxSize;
    //最大高度
    private int maxHeight;
    //密码长度
    private int passwordLength = 4;
    //间距
    private int passwordSpace = 0;
    private int rectColor = Color.WHITE;//矩形颜色
    private int rectBorderColor = Color.TRANSPARENT;//边框颜色
    private int rectBorderWidth = 0;//变框宽度
    private float radiusX, radiusY;
    //密码样式
    private Mode mode = Mode.RECT;
    private Paint paint;
    private Timer timer;
    private TimerTask timerTask;

    private boolean isCursorShowing;//光标是否正在显示
    private boolean isCursorEnable;//是否开启光标
    private boolean isInputComplete;//是否输入完毕
    private int cursorPosition;//光标位置
    private int cursorHeight;//光标高度
    private int cursorWidth;//光标宽度
    private int cursorColor;
    private long cursorFlashTime = 600;//光标闪动间隔时间

    private int textColor;//文字颜色
    private boolean textBold;//文字是否加粗
    //    private String CIPHER_TEXT = "●"; //密文符号
    private String CIPHER_TEXT = "•"; //密文符号
    private boolean cipherEnable;//是否绘制明文
    private String[] password;

    private InputMethodManager inputManager;

    public enum Mode {
        /**
         * 下划线
         */
        UNDERLINE(0),

        /**
         * 矩形
         */
        RECT(1),
        /**
         * 密码框相隔
         */
        SPACE(2);

        private int mode;

        Mode(int mode) {
            this.mode = mode;
        }

        public int getMode() {
            return this.mode;
        }

        static IPasswordView2.Mode formMode(int mode) {
            for (IPasswordView2.Mode m : values()) {
                if (mode == m.mode) {
                    return m;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public IPasswordView2(Context context) {
        this(context, null);
    }

    public IPasswordView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IPasswordView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int color = Color.GRAY;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TypedArray array = getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.colorAccent,});
            color = array.getColor(0, Color.GRAY);
            array.recycle();
        }
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.IPasswordView);
            mode = IPasswordView2.Mode.formMode(typedArray.getInteger(R.styleable.IPasswordView_pwdMode, IPasswordView2.Mode.RECT.getMode()));
            passwordLength = typedArray.getInteger(R.styleable.IPasswordView_pwdLength, 4);
            rectColor = typedArray.getColor(R.styleable.IPasswordView_pwdRectColor, Color.WHITE);
            rectBorderColor = typedArray.getColor(R.styleable.IPasswordView_pwdRectBorderColor, Color.TRANSPARENT);
            rectBorderWidth = Math.abs(typedArray.getDimensionPixelSize(R.styleable.IPasswordView_pwdRectBorderWidth, dp2px(1)));
            isCursorEnable = typedArray.getBoolean(R.styleable.IPasswordView_pwdCursorEnable, true);
            cursorColor = typedArray.getColor(R.styleable.IPasswordView_pwdCursorColor, color);
            cursorFlashTime = Math.abs(typedArray.getInteger(R.styleable.IPasswordView_pwdCursorFlashTime, 600));
            if (rectBorderWidth == 0) rectBorderWidth = 2;
            if (cursorFlashTime == 0) cursorFlashTime = 600;
            radiusX = typedArray.getDimensionPixelSize(R.styleable.IPasswordView_pwdRadiusx, 0);
            radiusY = typedArray.getDimensionPixelSize(R.styleable.IPasswordView_pwdRadiusy, 0);
            cursorWidth = typedArray.getDimensionPixelSize(R.styleable.IPasswordView_pwdCursorWidth, 6);
            //如果为边框样式，则padding 默认置为0
            if (mode != Mode.RECT) {
                passwordSpace = typedArray.getDimensionPixelSize(R.styleable.IPasswordView_pwdSpace, dp2px(15));
            } else {
                passwordSpace = typedArray.getDimensionPixelSize(R.styleable.IPasswordView_pwdSpace, 0);
            }
            textColor = typedArray.getColor(R.styleable.IPasswordView_pwdTextColor, Color.BLACK);
            textBold = typedArray.getBoolean(R.styleable.IPasswordView_pwdTextBold, false);
            cipherEnable = typedArray.getBoolean(R.styleable.IPasswordView_pwdCipherEnable, true);
            password = new String[passwordLength];
            typedArray.recycle();
        }
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = 0, height = 0;
        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                //没有指定大小，宽度 = 单个密码框大小 * 密码位数 + 密码框间距 *（密码位数 - 1）
                width = maxSize * passwordLength + passwordSpace * (passwordLength - 1);
                break;
            case MeasureSpec.EXACTLY:
                //指定大小，宽度 = 指定的大小
                width = MeasureSpec.getSize(widthMeasureSpec);
                //密码框大小等于 (宽度 - 密码框间距 *(密码位数 - 1)) / 密码位数
                break;
        }
        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                //没有指定大小
                height = maxHeight;
                break;
            case MeasureSpec.EXACTLY:
                //指定大小,
                height = MeasureSpec.getSize(heightMeasureSpec);
                if (height > maxHeight) height = maxHeight;
                break;
        }
        setMeasuredDimension(width, height);
    }

    private void init() {
        inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        maxHeight = dp2px(55);
        maxSize = dp2px(45);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                isCursorShowing = !isCursorShowing;
                postInvalidate();
            }
        };
        timer = new Timer();

        setBackgroundColor(Color.TRANSPARENT);
        setCursorVisible(false);
        setTextSize(0);
        setTextColor(Color.TRANSPARENT);
        setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER);
        addTextChangedListener(null);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(passwordLength)});
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mode) {
            case RECT:
                drawRect(canvas);
                break;
            case UNDERLINE:
                drawUnderLine(canvas);
                break;
            case SPACE:
                drawSpace(canvas);
                break;
        }
        //绘制光标
        drawCursor(canvas);
        //绘制密码文本
        drawCipherText(canvas);
    }

    private void drawSpace(Canvas canvas) {
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL);
        int passwordSize = (getWidth() - (passwordSpace * (passwordLength - 1))) / passwordLength;
        for (int i = 0; i < passwordLength; i++) {
            paint.setColor(rectBorderColor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.drawRoundRect((passwordSize + passwordSpace) * i,
                        0,
                        (passwordSize + passwordSpace) * i + passwordSize,
                        getHeight(),
                        radiusX, radiusY,
                        paint);
                paint.setColor(rectColor);
                canvas.drawRoundRect((passwordSize + passwordSpace) * i + rectBorderWidth,
                        rectBorderWidth,
                        (passwordSize + passwordSpace) * i + passwordSize - rectBorderWidth,
                        getHeight() - rectBorderWidth,
                        ((float) radiusX) / 2, ((float) radiusY) / 2,
                        paint);
            } else {
                canvas.drawRect((passwordSize + passwordSpace) * i,
                        0,
                        (passwordSize + passwordSpace) * i + passwordSize,
                        getHeight(),
                        paint);
                paint.setColor(rectColor);
                canvas.drawRect((passwordSize + passwordSpace) * i + rectBorderWidth,
                        rectBorderWidth,
                        (passwordSize + passwordSpace) * i + passwordSize - rectBorderWidth,
                        getHeight() - rectBorderWidth,
                        paint);
            }
        }
    }

    private void drawUnderLine(Canvas canvas) {
        paint.setColor(rectBorderColor);
        paint.setStrokeWidth(rectBorderWidth);
        paint.setStyle(Paint.Style.FILL);
        int passwordSize = (getWidth() - (passwordSpace * (passwordLength - 1))) / passwordLength;
        for (int i = 0; i < passwordLength; i++) {
            canvas.drawLine((passwordSize + passwordSpace) * i,
                    getHeight(),
                    (passwordSize + passwordSpace) * i + passwordSize, getHeight(),
                    paint);
        }
    }

    private void drawRect(Canvas canvas) {
        float borderWidth = ((float) rectBorderWidth) / 2;
        paint.setColor(rectBorderColor);
        paint.setStrokeWidth(rectBorderWidth);
        paint.setStyle(Paint.Style.STROKE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(borderWidth, borderWidth, getWidth() - borderWidth, getHeight() - borderWidth, radiusX, radiusY, paint);
        } else {
            canvas.drawRect(borderWidth, borderWidth, getWidth() - borderWidth, getHeight() - borderWidth, paint);
        }
        int size = getWidth() / passwordLength;
        //画分割线
        paint.setStrokeWidth(rectBorderWidth);
        for (int i = 0; i < passwordLength; i++) {
            if (i > 0) {
                canvas.drawLine(i * size + rectBorderWidth, 0, i * size + rectBorderWidth, getHeight(), paint);
            }
        }
    }

    private void drawCursor(Canvas canvas) {
        paint.setColor(cursorColor);
        paint.setStrokeWidth(cursorWidth);
        paint.setStyle(Paint.Style.FILL);
        if (cursorHeight >= getHeight() || cursorHeight == 0) {
            cursorHeight = getHeight() * 3 / 5; //做下兼容，高度的五分之三
        }
        //光标未显示 && 开启光标 && 输入位数未满 && 获得焦点
        if (!isCursorShowing && isCursorEnable && !isInputComplete && hasFocus()) {
            if (mode == Mode.RECT) {
                int passwordSize = getWidth() / passwordLength;
                canvas.drawLine((passwordSize / 2) + (passwordSize) * cursorPosition,
                        (getHeight() - cursorHeight) / 2,
                        (passwordSize / 2) + (passwordSize) * cursorPosition,
                        (getHeight() + cursorHeight) / 2,
                        paint);
            } else {
                int passwordSize = (getWidth() - (passwordSpace * (passwordLength - 1))) / passwordLength;
                canvas.drawLine(cursorPosition * (passwordSpace + passwordSize) + passwordSize / 2,
                        (getHeight() - cursorHeight) / 2,
                        cursorPosition * (passwordSpace + passwordSize) + passwordSize / 2,
                        (getHeight() + cursorHeight) / 2,
                        paint);
            }
        }
    }

    private void drawCipherText(Canvas canvas) {
        if (password != null) {
            //画笔初始化
            paint.setColor(textColor);
            paint.setTextSize(getHeight() * 3 / 5);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setStyle(Paint.Style.FILL);
            paint.setFakeBoldText(textBold);

            //文字居中的处理
            int textHeight = getTextHeight(paint, cipherEnable ? CIPHER_TEXT : (TextUtils.isEmpty(password[0]) ? "0" : password[0]));

            float drawX = 0;
            Rect r = new Rect();
            canvas.getClipBounds(r);
            int cHeight = r.height();
            paint.getTextBounds(CIPHER_TEXT, 0, CIPHER_TEXT.length(), r);
            float y = cHeight / 2f + r.height() / 2f - r.bottom;
            float drawY = cipherEnable ? y : ((getHeight() + textHeight) / 2);
            for (int i = 0; i < password.length; i++) {
                if (!TextUtils.isEmpty(password[i])) {
                    if (mode == Mode.RECT) {
                        int size = getWidth() / passwordLength;
                        drawX = (size / 2) + (size) * i;
                    } else {
                        int size = (getWidth() - (passwordSpace * (passwordLength - 1))) / passwordLength;
                        drawX = (size / 2) + (size + passwordSpace) * i;
                    }
                    canvas.drawText(cipherEnable ? CIPHER_TEXT : password[i], drawX, drawY, paint);
                }
            }
        }
    }

    private int getTextHeight(Paint paint, String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //弹出软键盘
            setFocusableInTouchMode(true);
            requestFocus();
//            inputManager.showSoftInput(this, InputMethodManager.SHOW_FORCED);
            performClick();
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        super.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > passwordLength)
                    return;
                if (count == 0) {
                    String deleteText = delete();
                    if (passwordListener != null && !TextUtils.isEmpty(deleteText)) {
                        passwordListener.passwordChange(IPasswordView2.this, deleteText);
                    }
                }
                if (count == 1) {
                    String changedText = s.toString().substring(s.length() - 1, s.length()) + "";
                    if (passwordListener != null && !TextUtils.isEmpty(changedText)) {
                        passwordListener.passwordChange(IPasswordView2.this, changedText);
                    }
                    String addText = add(changedText);
                    if (passwordListener != null) {
                        passwordListener.passwordCompleteFinish(IPasswordView2.this, getPassword(), isInputComplete);
                    }
                    postInvalidate();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private String delete() {
        String deleteText = null;
        if (cursorPosition > 0) {
            deleteText = password[cursorPosition - 1];
            password[cursorPosition - 1] = null;
            cursorPosition--;
        } else if (cursorPosition == 0) {
            deleteText = password[cursorPosition];
            password[cursorPosition] = null;
        }
        isInputComplete = false;
        return deleteText;
    }

    private String add(String c) {
        String addText = null;
        if (cursorPosition < passwordLength) {
            addText = c;
            password[cursorPosition] = c;
            cursorPosition++;
            if (cursorPosition == passwordLength) {
                isInputComplete = true;
                if (passwordListener != null) {
                    passwordListener.passwordComplete(IPasswordView2.this);
                }
            }
        }
        return addText;
    }

    public String getPassword() {
        StringBuilder stringBuffer = new StringBuilder();
        for (String c : password) {
            if (TextUtils.isEmpty(c)) {
                continue;
            }
            stringBuffer.append(c);
        }
        return stringBuffer.toString();
    }

    private IPasswordView2.PasswordListener passwordListener;

    public void setPasswordListener(IPasswordView2.PasswordListener passwordListener) {
        this.passwordListener = passwordListener;
    }

    public interface PasswordListener {
        /**
         * 输入/删除监听
         *
         * @param view
         * @param changeText 输入/删除的字符
         */
        void passwordChange(IPasswordView2 view, String changeText);

        /**
         * 输入完成
         */
        void passwordComplete(IPasswordView2 view);

        void passwordCompleteFinish(IPasswordView2 view, String pwd, boolean isComplete);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //cursorFlashTime为光标闪动的间隔时间
        if (timer != null) {
            timer.scheduleAtFixedRate(timerTask, 0, cursorFlashTime);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        setFocusableInTouchMode(false);
        setFocusable(false);
        inputManager.hideSoftInputFromWindow(getWindowToken(), 2);
        super.onDetachedFromWindow();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(0, 0, 0, 0);
    }

    private int dp2px(float dp) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
