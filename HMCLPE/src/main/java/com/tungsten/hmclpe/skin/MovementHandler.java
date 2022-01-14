package com.tungsten.hmclpe.skin;

import android.view.MotionEvent;

public class MovementHandler {
    public static final float DISTANCE_FACTOR = 20.0F;
    public static final float MAX_ANGLE_XMOVEMENT = 0.2617994F;
    public static final float MAX_ANGLE_YMOVEMENT = 0.2617994F;
    public static final float MAX_MOVEMENT_DELTA = 5.0F;
    public static final float MIN_ANGLE_XMOVEMENT = 0.004363323F;
    public static final float MIN_ANGLE_YMOVEMENT = 0.004363323F;
    public static final float MIN_MOVEMENT_DELTA = 0.2F;
    public static final float MOVEMENT_X_FACTOR = 10.0F;
    public static final float MOVEMENT_Y_FACTOR = 10.0F;
    public static final float XANGLE_FACTOR = 420.0F;
    public static final float YANGLE_FACTOR = 420.0F;
    private int _firstPointerId = -1;
    private int _secondPointerId = -1;
    private float lastAngle;
    private float lastDistance;
    private float lastMovementx1;
    private float lastMovementx2;
    private float lastMovementy1;
    private float lastMovementy2;
    private float startAngle;
    private float startDistance;
    private float startMovementx1;
    private float startMovementx2;
    private float startMovementy1;
    private float startMovementy2;

    public MovementHandler() {
    }

    private float calcAngle() {
        return (float)Math.atan2((double)(this.lastMovementx1 - this.lastMovementx2), (double)(this.lastMovementy1 - this.lastMovementy2));
    }

    private float calcDistance() {
        float var1 = this.lastMovementx2 - this.lastMovementx1;
        float var2 = this.lastMovementy2 - this.lastMovementy1;
        return (float)Math.sqrt((double)(var1 * var1 + var2 * var2));
    }

    public Movement getMovement() {
        synchronized(this){}

        Throwable var10000;
        label2511: {
            Movement var1;
            boolean var10001;
            label2516: {
                float var2;
                float var3;
                label2528: {
                    float var4;
                    float var5;
                    try {
                        var1 = new Movement();
                        if (this._secondPointerId == -1) {
                            break label2528;
                        }

                        var2 = this.lastMovementx1 - this.startMovementx1;
                        var3 = this.lastMovementy1 - this.startMovementy1;
                        var4 = this.lastMovementx2 - this.startMovementx2;
                        var5 = this.lastMovementy2 - this.startMovementy2;
                    } catch (Throwable var277) {
                        var10000 = var277;
                        var10001 = false;
                        break label2511;
                    }

                    if (var2 > 0.0F && var4 > 0.0F || var2 < 0.0F && var4 < 0.0F) {
                        try {
                            var4 = Math.min(Math.abs(var2), Math.abs(var4)) / 10.0F;
                        } catch (Throwable var275) {
                            var10000 = var275;
                            var10001 = false;
                            break label2511;
                        }

                        if (var4 > 0.2F && var4 < 5.0F) {
                            if (var2 > 0.0F) {
                                try {
                                    var1.cameraMovementX = var4;
                                } catch (Throwable var274) {
                                    var10000 = var274;
                                    var10001 = false;
                                    break label2511;
                                }
                            } else {
                                try {
                                    var1.cameraMovementX = -var4;
                                } catch (Throwable var273) {
                                    var10000 = var273;
                                    var10001 = false;
                                    break label2511;
                                }
                            }
                        }
                    }

                    if (var3 > 0.0F && var5 > 0.0F || var3 < 0.0F && var5 < 0.0F) {
                        try {
                            var2 = Math.min(Math.abs(var3), Math.abs(var5)) / 10.0F;
                        } catch (Throwable var272) {
                            var10000 = var272;
                            var10001 = false;
                            break label2511;
                        }

                        if (var2 > 0.2F && var2 < 5.0F) {
                            if (var3 > 0.0F) {
                                try {
                                    var1.cameraMovementY = var2;
                                } catch (Throwable var271) {
                                    var10000 = var271;
                                    var10001 = false;
                                    break label2511;
                                }
                            } else {
                                try {
                                    var1.cameraMovementY = -var2;
                                } catch (Throwable var270) {
                                    var10000 = var270;
                                    var10001 = false;
                                    break label2511;
                                }
                            }
                        }
                    }

                    try {
                        var3 = (this.lastDistance - this.startDistance) / 20.0F;
                        var2 = Math.abs(var3);
                    } catch (Throwable var269) {
                        var10000 = var269;
                        var10001 = false;
                        break label2511;
                    }

                    if (var2 > 0.2F && var2 < 5.0F) {
                        try {
                            var1.cameraMovementZ = var3;
                        } catch (Throwable var268) {
                            var10000 = var268;
                            var10001 = false;
                            break label2511;
                        }
                    }

                    try {
                        var2 = this.lastAngle - this.startAngle;
                        var3 = Math.abs(var2);
                    } catch (Throwable var267) {
                        var10000 = var267;
                        var10001 = false;
                        break label2511;
                    }

                    if (var3 > 0.004363323F && var3 < 0.2617994F) {
                        try {
                            var1.cameraRotationY = var2;
                        } catch (Throwable var266) {
                            var10000 = var266;
                            var10001 = false;
                            break label2511;
                        }
                    }
                    break label2516;
                }

                try {
                    if (this._firstPointerId == -1) {
                        break label2516;
                    }

                    var2 = (this.lastMovementx1 - this.startMovementx1) / 420.0F;
                    var3 = Math.abs(var2);
                } catch (Throwable var276) {
                    var10000 = var276;
                    var10001 = false;
                    break label2511;
                }

                if (var3 > 0.004363323F && var3 < 0.2617994F) {
                    try {
                        var1.cameraRotationY = var2;
                    } catch (Throwable var265) {
                        var10000 = var265;
                        var10001 = false;
                        break label2511;
                    }
                }

                try {
                    var2 = (this.lastMovementy1 - this.startMovementy1) / 420.0F;
                    var3 = Math.abs(var2);
                } catch (Throwable var264) {
                    var10000 = var264;
                    var10001 = false;
                    break label2511;
                }

                if (var3 > 0.004363323F && var3 < 0.2617994F) {
                    try {
                        var1.worldRotationX = var2;
                    } catch (Throwable var263) {
                        var10000 = var263;
                        var10001 = false;
                        break label2511;
                    }
                }
            }

            label2441:
            try {
                this.startMovementx1 = this.lastMovementx1;
                this.startMovementy1 = this.lastMovementy1;
                this.startMovementx2 = this.lastMovementx2;
                this.startMovementy2 = this.lastMovementy2;
                this.startDistance = this.lastDistance;
                this.startAngle = this.lastAngle;
                return var1;
            } catch (Throwable var262) {
                var10000 = var262;
                var10001 = false;
                break label2441;
            }
        }

        return null;
    }

    public void handleMotionEvent(MotionEvent var1) {
        synchronized(this){}

        Throwable var10000;
        label1250: {
            int var2;
            int var3;
            int var4;
            boolean var10001;
            try {
                var2 = var1.getActionIndex();
                var3 = var1.getPointerId(var2);
                var4 = var1.getActionMasked();
            } catch (Throwable var139) {
                var10000 = var139;
                var10001 = false;
                break label1250;
            }

            float var7;
            if (var4 != 0) {
                byte var5 = 1;
                byte var6 = 1;
                if (var4 != 1) {
                    if (var4 != 2) {
                        if (var4 != 5) {
                            if (var4 != 6) {
                                return;
                            }

                            label1236: {
                                try {
                                    if (var3 != this._firstPointerId) {
                                        break label1236;
                                    }
                                } catch (Throwable var133) {
                                    var10000 = var133;
                                    var10001 = false;
                                    break label1250;
                                }

                                if (var2 != 0) {
                                    var6 = 0;
                                }

                                try {
                                    this._firstPointerId = var1.getPointerId(var6);
                                    this.startMovementx1 = var1.getX(var6);
                                    this.startMovementy1 = var1.getY(var6);
                                    return;
                                } catch (Throwable var129) {
                                    var10000 = var129;
                                    var10001 = false;
                                    break label1250;
                                }
                            }

                            label1202: {
                                try {
                                    if (var3 != this._secondPointerId) {
                                        return;
                                    }

                                    if (var1.getPointerCount() > 2) {
                                        break label1202;
                                    }
                                } catch (Throwable var132) {
                                    var10000 = var132;
                                    var10001 = false;
                                    break label1250;
                                }

                                try {
                                    this._secondPointerId = -1;
                                    return;
                                } catch (Throwable var131) {
                                    var10000 = var131;
                                    var10001 = false;
                                    break label1250;
                                }
                            }

                            var6 = var5;
                            if (var2 == 1) {
                                var6 = 2;
                            }

                            try {
                                this._secondPointerId = var1.getPointerId(var6);
                                this.startMovementx2 = var1.getX(var6);
                                this.startMovementy2 = var1.getY(var6);
                            } catch (Throwable var130) {
                                var10000 = var130;
                                var10001 = false;
                                break label1250;
                            }
                        } else {
                            try {
                                if (var1.getPointerCount() == 2) {
                                    this._secondPointerId = var3;
                                    this.startMovementx2 = var1.getX(var3);
                                    var7 = var1.getY(this._secondPointerId);
                                    this.startMovementy2 = var7;
                                    this.lastMovementx2 = this.startMovementx2;
                                    this.lastMovementy2 = var7;
                                    this.startDistance = this.calcDistance();
                                    this.startAngle = this.calcAngle();
                                    return;
                                }
                            } catch (Throwable var134) {
                                var10000 = var134;
                                var10001 = false;
                                break label1250;
                            }
                        }

                        return;
                    } else {
                        int var141;
                        try {
                            int var142 = var1.findPointerIndex(this._firstPointerId);
                            var141 = var1.findPointerIndex(this._secondPointerId);
                            if (var1.getPointerCount() > 0) {
                                this.lastMovementx1 = var1.getX(var142);
                                this.lastMovementy1 = var1.getY(var142);
                            }
                        } catch (Throwable var136) {
                            var10000 = var136;
                            var10001 = false;
                            break label1250;
                        }

                        try {
                            if (var1.getPointerCount() > 1) {
                                this.lastMovementx2 = var1.getX(var141);
                                this.lastMovementy2 = var1.getY(var141);
                                this.lastDistance = this.calcDistance();
                                this.lastAngle = this.calcAngle();
                                return;
                            }
                        } catch (Throwable var135) {
                            var10000 = var135;
                            var10001 = false;
                            break label1250;
                        }
                    }

                    return;
                } else {
                    try {
                        this._firstPointerId = -1;
                        this._secondPointerId = -1;
                    } catch (Throwable var137) {
                        var10000 = var137;
                        var10001 = false;
                        break label1250;
                    }
                }

                return;
            } else {
                try {
                    this._firstPointerId = var3;
                    this._secondPointerId = -1;
                    this.startMovementx1 = var1.getX(var3);
                    var7 = var1.getY(this._firstPointerId);
                    this.startMovementy1 = var7;
                    this.lastMovementx1 = this.startMovementx1;
                    this.lastMovementy1 = var7;
                } catch (Throwable var138) {
                    var10000 = var138;
                    var10001 = false;
                    break label1250;
                }
            }

            return;
        }

        return;
    }
}
