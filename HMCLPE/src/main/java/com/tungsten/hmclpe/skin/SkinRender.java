package com.tungsten.hmclpe.skin;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class SkinRender
{
    public static Bitmap FrontAndBackTogether(Bitmap scaledBitmap, final Bitmap bitmap) {
        scaledBitmap = Bitmap.createScaledBitmap(scaledBitmap, (int)(scaledBitmap.getWidth() * 1.125), (int)(scaledBitmap.getHeight() * 1.125), false);
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), scaledBitmap.getHeight() + bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        for (int i = 0; i < bitmap.getWidth(); ++i) {
            for (int j = 0; j < bitmap.getHeight(); ++j) {
                if (bitmap.getPixel(i, j) != 0) {
                    bitmap2.setPixel(i, bitmap.getHeight() + j, bitmap.getPixel(i, j));
                }
            }
        }
        for (int k = 0; k < scaledBitmap.getWidth(); ++k) {
            for (int l = 0; l < scaledBitmap.getHeight(); ++l) {
                if (scaledBitmap.getPixel(k, l) != 0) {
                    bitmap2.setPixel((bitmap.getWidth() - scaledBitmap.getWidth()) / 2 + k, l + 5, scaledBitmap.getPixel(k, l));
                }
            }
        }
        scaledBitmap.recycle();
        return bitmap2;
    }
    
    public static Bitmap addBorder(final Bitmap bitmap, final int n) {
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth() + n * 2, bitmap.getHeight() + n * 2, bitmap.getConfig());
        final Canvas canvas = new Canvas(bitmap2);
        canvas.drawColor(0);
        canvas.drawBitmap(bitmap, (float)n, (float)n, (Paint)null);
        return bitmap2;
    }
    
    public static Bitmap convertSkin(Bitmap bitmap) {
        final Bitmap bitmap2 = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < bitmap.getWidth(); ++i) {
            for (int j = 0; j < bitmap.getHeight(); ++j) {
                bitmap2.setPixel(i, j, bitmap.getPixel(i, j));
            }
        }
        final Bitmap bitmap3 = Bitmap.createBitmap(bitmap, BodyPart.RightArmSkin.RIGHT.getStartX(), BodyPart.RightArmSkin.TOP.getStartY(), 16, 16);
        bitmap = Bitmap.createBitmap(bitmap, BodyPart.RightLegSkin.RIGHT.getStartX(), BodyPart.RightLegSkin.TOP.getStartY(), 16, 16);
        for (int k = 0; k < bitmap3.getWidth(); ++k) {
            for (int l = 0; l < bitmap3.getHeight(); ++l) {
                if (bitmap3.getPixel(k, l) != 0) {
                    bitmap2.setPixel(BodyPart.LeftArmSkin.RIGHT.getStartX() + k, BodyPart.LeftArmSkin.TOP.getStartY() + l, bitmap3.getPixel(k, l));
                }
            }
        }
        for (int n = 0; n < bitmap.getWidth(); ++n) {
            for (int n2 = 0; n2 < bitmap.getHeight(); ++n2) {
                if (bitmap.getPixel(n, n2) != 0) {
                    bitmap2.setPixel(BodyPart.LeftLegSkin.RIGHT.getStartX() + n, BodyPart.LeftLegSkin.TOP.getStartY() + n2, bitmap.getPixel(n, n2));
                }
            }
        }
        return bitmap2;
    }
    
    public static Bitmap convertToMutable(Bitmap bitmap) {
        Bitmap bitmap2 = bitmap;
        Bitmap bitmap3 = bitmap;
        try {
            final File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temp.tmp");
            bitmap2 = bitmap;
            bitmap3 = bitmap;
            final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            bitmap2 = bitmap;
            bitmap3 = bitmap;
            final int width = bitmap.getWidth();
            bitmap2 = bitmap;
            bitmap3 = bitmap;
            final int height = bitmap.getHeight();
            bitmap2 = bitmap;
            bitmap3 = bitmap;
            final Bitmap.Config config = bitmap.getConfig();
            bitmap2 = bitmap;
            bitmap3 = bitmap;
            final FileChannel channel = randomAccessFile.getChannel();
            bitmap2 = bitmap;
            bitmap3 = bitmap;
            final MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0L, bitmap.getRowBytes() * height);
            bitmap2 = bitmap;
            bitmap3 = bitmap;
            bitmap.copyPixelsToBuffer((Buffer)map);
            bitmap2 = bitmap;
            bitmap3 = bitmap;
            bitmap.recycle();
            bitmap2 = bitmap;
            bitmap3 = bitmap;
            System.gc();
            bitmap2 = bitmap;
            bitmap3 = bitmap;
            bitmap = (bitmap3 = (bitmap2 = Bitmap.createBitmap(width, height, config)));
            map.position(0);
            bitmap2 = bitmap;
            bitmap3 = bitmap;
            bitmap.copyPixelsFromBuffer((Buffer)map);
            bitmap2 = bitmap;
            bitmap3 = bitmap;
            channel.close();
            bitmap2 = bitmap;
            bitmap3 = bitmap;
            randomAccessFile.close();
            bitmap2 = bitmap;
            bitmap3 = bitmap;
            file.delete();
            return bitmap;
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return bitmap2;
        }
        catch (IOException ex2) {
            ex2.printStackTrace();
            return bitmap3;
        }
    }
    
    public static Bitmap getBodyBackOfSkin(Bitmap bitmap) {
        Bitmap convertSkin = bitmap;
        if (isOldSkin(bitmap)) {
            convertSkin = convertSkin(bitmap);
        }
        bitmap = Bitmap.createBitmap(convertSkin, BodyPart.LeftArmSkin.BACK.getStartX(), BodyPart.LeftArmSkin.BACK.getStartY(), BodyPart.LeftArmSkin.BACK.getWidth(), BodyPart.LeftArmSkin.BACK.getHeight());
        final Bitmap bitmap2 = Bitmap.createBitmap(convertSkin, BodyPart.RightArmSkin.BACK.getStartX(), BodyPart.RightArmSkin.BACK.getStartY(), BodyPart.RightArmSkin.BACK.getWidth(), BodyPart.RightArmSkin.BACK.getHeight());
        final Bitmap bitmap3 = Bitmap.createBitmap(convertSkin, BodyPart.BodySkin.BACK.getStartX(), BodyPart.BodySkin.BACK.getStartY(), BodyPart.BodySkin.BACK.getWidth(), BodyPart.BodySkin.BACK.getHeight());
        final Bitmap bitmap4 = Bitmap.createBitmap(convertSkin, BodyPart.LeftLegSkin.BACK.getStartX(), BodyPart.LeftLegSkin.BACK.getStartY(), BodyPart.LeftLegSkin.BACK.getWidth(), BodyPart.LeftLegSkin.BACK.getHeight());
        final Bitmap bitmap5 = Bitmap.createBitmap(convertSkin, BodyPart.RightLegSkin.BACK.getStartX(), BodyPart.RightLegSkin.BACK.getStartY(), BodyPart.RightLegSkin.BACK.getWidth(), BodyPart.RightLegSkin.BACK.getHeight());
        final Bitmap bitmap6 = Bitmap.createBitmap(convertSkin, BodyPart.LeftSleeveSkin.BACK.getStartX(), BodyPart.LeftSleeveSkin.BACK.getStartY(), BodyPart.LeftSleeveSkin.BACK.getWidth(), BodyPart.LeftSleeveSkin.BACK.getHeight());
        final Bitmap bitmap7 = Bitmap.createBitmap(convertSkin, BodyPart.RightSleeveSkin.BACK.getStartX(), BodyPart.RightSleeveSkin.BACK.getStartY(), BodyPart.RightSleeveSkin.BACK.getWidth(), BodyPart.RightSleeveSkin.BACK.getHeight());
        final Bitmap bitmap8 = Bitmap.createBitmap(convertSkin, BodyPart.JacketSkin.BACK.getStartX(), BodyPart.JacketSkin.BACK.getStartY(), BodyPart.JacketSkin.BACK.getWidth(), BodyPart.JacketSkin.BACK.getHeight());
        final Bitmap bitmap9 = Bitmap.createBitmap(convertSkin, BodyPart.LeftLegOverlaySkin.BACK.getStartX(), BodyPart.LeftLegOverlaySkin.BACK.getStartY(), BodyPart.LeftLegOverlaySkin.BACK.getWidth(), BodyPart.LeftLegOverlaySkin.BACK.getHeight());
        final Bitmap bitmap10 = Bitmap.createBitmap(convertSkin, BodyPart.RightLegOverlaySkin.BACK.getStartX(), BodyPart.RightLegOverlaySkin.BACK.getStartY(), BodyPart.RightLegOverlaySkin.BACK.getWidth(), BodyPart.RightLegOverlaySkin.BACK.getHeight());
        final Bitmap bitmap11 = Bitmap.createBitmap(bitmap.getWidth() + bitmap2.getWidth() + bitmap3.getWidth(), bitmap.getHeight() + bitmap4.getHeight(), Bitmap.Config.ARGB_8888);
        for (int i = 0; i < bitmap.getWidth(); ++i) {
            for (int j = 0; j < bitmap.getHeight(); ++j) {
                bitmap11.setPixel(i, j, bitmap.getPixel(i, j));
            }
        }
        for (int k = 0; k < bitmap3.getWidth(); ++k) {
            for (int l = 0; l < bitmap3.getHeight(); ++l) {
                bitmap11.setPixel(bitmap.getWidth() + k, l, bitmap3.getPixel(k, l));
            }
        }
        for (int n = 0; n < bitmap2.getWidth(); ++n) {
            for (int n2 = 0; n2 < bitmap2.getHeight(); ++n2) {
                bitmap11.setPixel(bitmap2.getWidth() + n + bitmap3.getWidth(), n2, bitmap2.getPixel(n, n2));
            }
        }
        for (int n3 = 0; n3 < bitmap4.getWidth(); ++n3) {
            for (int n4 = 0; n4 < bitmap4.getHeight(); ++n4) {
                bitmap11.setPixel(bitmap4.getWidth() + n3, bitmap3.getHeight() + n4, bitmap4.getPixel(n3, n4));
            }
        }
        for (int n5 = 0; n5 < bitmap5.getWidth(); ++n5) {
            for (int n6 = 0; n6 < bitmap5.getHeight(); ++n6) {
                bitmap11.setPixel(bitmap5.getWidth() + n5 + bitmap4.getWidth(), bitmap3.getHeight() + n6, bitmap5.getPixel(n5, n6));
            }
        }
        for (int n7 = 0; n7 < bitmap6.getWidth(); ++n7) {
            for (int n8 = 0; n8 < bitmap6.getHeight(); ++n8) {
                if (bitmap6.getPixel(n7, n8) != 0) {
                    bitmap11.setPixel(n7, n8, bitmap6.getPixel(n7, n8));
                }
            }
        }
        for (int n9 = 0; n9 < bitmap7.getWidth(); ++n9) {
            for (int n10 = 0; n10 < bitmap7.getHeight(); ++n10) {
                if (bitmap7.getPixel(n9, n10) != 0) {
                    bitmap11.setPixel(bitmap.getWidth() + n9 + bitmap8.getWidth(), n10, bitmap7.getPixel(n9, n10));
                }
            }
        }
        for (int n11 = 0; n11 < bitmap9.getWidth(); ++n11) {
            for (int n12 = 0; n12 < bitmap9.getHeight(); ++n12) {
                if (bitmap9.getPixel(n11, n12) != 0) {
                    bitmap11.setPixel(bitmap.getWidth() + n11, bitmap3.getHeight() + n12, bitmap9.getPixel(n11, n12));
                }
            }
        }
        for (int n13 = 0; n13 < bitmap10.getWidth(); ++n13) {
            for (int n14 = 0; n14 < bitmap10.getHeight(); ++n14) {
                if (bitmap10.getPixel(n13, n14) != 0) {
                    bitmap11.setPixel(bitmap.getWidth() + n13 + bitmap4.getWidth(), bitmap3.getHeight() + n14, bitmap10.getPixel(n13, n14));
                }
            }
        }
        bitmap.recycle();
        bitmap2.recycle();
        bitmap3.recycle();
        bitmap4.recycle();
        bitmap5.recycle();
        bitmap7.recycle();
        bitmap8.recycle();
        bitmap9.recycle();
        bitmap10.recycle();
        return bitmap11;
    }
    
    public static Bitmap getBodyFrontOfSkin(Bitmap bitmap) {
        Bitmap convertSkin = bitmap;
        if (isOldSkin(bitmap)) {
            convertSkin = convertSkin(bitmap);
        }
        bitmap = Bitmap.createBitmap(convertSkin, BodyPart.LeftArmSkin.FRONT.getStartX(), BodyPart.LeftArmSkin.FRONT.getStartY(), BodyPart.LeftArmSkin.FRONT.getWidth(), BodyPart.LeftArmSkin.FRONT.getHeight());
        final Bitmap bitmap2 = Bitmap.createBitmap(convertSkin, BodyPart.RightArmSkin.FRONT.getStartX(), BodyPart.RightArmSkin.FRONT.getStartY(), BodyPart.RightArmSkin.FRONT.getWidth(), BodyPart.RightArmSkin.FRONT.getHeight());
        final Bitmap bitmap3 = Bitmap.createBitmap(convertSkin, BodyPart.BodySkin.FRONT.getStartX(), BodyPart.BodySkin.FRONT.getStartY(), BodyPart.BodySkin.FRONT.getWidth(), BodyPart.BodySkin.FRONT.getHeight());
        final Bitmap bitmap4 = Bitmap.createBitmap(convertSkin, BodyPart.LeftLegSkin.FRONT.getStartX(), BodyPart.LeftLegSkin.FRONT.getStartY(), BodyPart.LeftLegSkin.FRONT.getWidth(), BodyPart.LeftLegSkin.FRONT.getHeight());
        final Bitmap bitmap5 = Bitmap.createBitmap(convertSkin, BodyPart.RightLegSkin.FRONT.getStartX(), BodyPart.RightLegSkin.FRONT.getStartY(), BodyPart.RightLegSkin.FRONT.getWidth(), BodyPart.RightLegSkin.FRONT.getHeight());
        final Bitmap bitmap6 = Bitmap.createBitmap(convertSkin, BodyPart.LeftSleeveSkin.FRONT.getStartX(), BodyPart.LeftSleeveSkin.FRONT.getStartY(), BodyPart.LeftSleeveSkin.FRONT.getWidth(), BodyPart.LeftSleeveSkin.FRONT.getHeight());
        final Bitmap bitmap7 = Bitmap.createBitmap(convertSkin, BodyPart.RightSleeveSkin.FRONT.getStartX(), BodyPart.RightSleeveSkin.FRONT.getStartY(), BodyPart.RightSleeveSkin.FRONT.getWidth(), BodyPart.RightSleeveSkin.FRONT.getHeight());
        final Bitmap bitmap8 = Bitmap.createBitmap(convertSkin, BodyPart.JacketSkin.FRONT.getStartX(), BodyPart.JacketSkin.FRONT.getStartY(), BodyPart.JacketSkin.FRONT.getWidth(), BodyPart.JacketSkin.FRONT.getHeight());
        final Bitmap bitmap9 = Bitmap.createBitmap(convertSkin, BodyPart.LeftLegOverlaySkin.FRONT.getStartX(), BodyPart.LeftLegOverlaySkin.FRONT.getStartY(), BodyPart.LeftLegOverlaySkin.FRONT.getWidth(), BodyPart.LeftLegOverlaySkin.FRONT.getHeight());
        final Bitmap bitmap10 = Bitmap.createBitmap(convertSkin, BodyPart.RightLegOverlaySkin.FRONT.getStartX(), BodyPart.RightLegOverlaySkin.FRONT.getStartY(), BodyPart.RightLegOverlaySkin.FRONT.getWidth(), BodyPart.RightLegOverlaySkin.FRONT.getHeight());
        final Bitmap bitmap11 = Bitmap.createBitmap(bitmap.getWidth() + bitmap2.getWidth() + bitmap3.getWidth(), bitmap.getHeight() + bitmap4.getHeight(), Bitmap.Config.ARGB_8888);
        for (int i = 0; i < bitmap.getWidth(); ++i) {
            for (int j = 0; j < bitmap.getHeight(); ++j) {
                bitmap11.setPixel(i, j, bitmap2.getPixel(i, j));
            }
        }
        for (int k = 0; k < bitmap3.getWidth(); ++k) {
            for (int l = 0; l < bitmap3.getHeight(); ++l) {
                bitmap11.setPixel(bitmap.getWidth() + k, l, bitmap3.getPixel(k, l));
            }
        }
        for (int n = 0; n < bitmap2.getWidth(); ++n) {
            for (int n2 = 0; n2 < bitmap2.getHeight(); ++n2) {
                bitmap11.setPixel(bitmap.getWidth() + n + bitmap3.getWidth(), n2, bitmap.getPixel(n, n2));
            }
        }
        for (int n3 = 0; n3 < bitmap4.getWidth(); ++n3) {
            for (int n4 = 0; n4 < bitmap4.getHeight(); ++n4) {
                bitmap11.setPixel(bitmap.getWidth() + n3, bitmap3.getHeight() + n4, bitmap5.getPixel(n3, n4));
            }
        }
        for (int n5 = 0; n5 < bitmap5.getWidth(); ++n5) {
            for (int n6 = 0; n6 < bitmap5.getHeight(); ++n6) {
                bitmap11.setPixel(bitmap.getWidth() + n5 + bitmap4.getWidth(), bitmap3.getHeight() + n6, bitmap4.getPixel(n5, n6));
            }
        }
        for (int n7 = 0; n7 < bitmap7.getWidth(); ++n7) {
            for (int n8 = 0; n8 < bitmap7.getHeight(); ++n8) {
                if (bitmap7.getPixel(n7, n8) != 0) {
                    bitmap11.setPixel(n7, n8, bitmap7.getPixel(n7, n8));
                }
            }
        }
        for (int n9 = 0; n9 < bitmap8.getWidth(); ++n9) {
            for (int n10 = 0; n10 < bitmap8.getHeight(); ++n10) {
                if (bitmap8.getPixel(n9, n10) != 0) {
                    bitmap11.setPixel(bitmap6.getWidth() + n9, n10, bitmap8.getPixel(n9, n10));
                }
            }
        }
        for (int n11 = 0; n11 < bitmap6.getWidth(); ++n11) {
            for (int n12 = 0; n12 < bitmap6.getHeight(); ++n12) {
                if (bitmap6.getPixel(n11, n12) != 0) {
                    bitmap11.setPixel(bitmap.getWidth() + n11 + bitmap3.getWidth(), n12, bitmap6.getPixel(n11, n12));
                }
            }
        }
        for (int n13 = 0; n13 < bitmap9.getWidth(); ++n13) {
            for (int n14 = 0; n14 < bitmap9.getHeight(); ++n14) {
                if (bitmap9.getPixel(n13, n14) != 0) {
                    bitmap11.setPixel(bitmap.getWidth() + n13, bitmap3.getHeight() + n14, bitmap9.getPixel(n13, n14));
                }
            }
        }
        for (int n15 = 0; n15 < bitmap10.getWidth(); ++n15) {
            for (int n16 = 0; n16 < bitmap10.getHeight(); ++n16) {
                if (bitmap10.getPixel(n15, n16) != 0) {
                    bitmap11.setPixel(bitmap.getWidth() + n15 + bitmap4.getWidth(), bitmap3.getHeight() + n16, bitmap10.getPixel(n15, n16));
                }
            }
        }
        bitmap.recycle();
        bitmap2.recycle();
        bitmap3.recycle();
        bitmap4.recycle();
        bitmap5.recycle();
        bitmap6.recycle();
        bitmap7.recycle();
        bitmap8.recycle();
        bitmap9.recycle();
        bitmap10.recycle();
        return bitmap11;
    }
    
    public static Bitmap getBodyOfAlexSkin(Bitmap bitmap) {
        Bitmap convertSkin = bitmap;
        if (isOldSkin(bitmap)) {
            convertSkin = convertSkin(bitmap);
        }
        bitmap = Bitmap.createBitmap(convertSkin, BodyPart.AlexLeftArmSkin.FRONT.getStartX(), BodyPart.AlexLeftArmSkin.FRONT.getStartY(), BodyPart.AlexLeftArmSkin.FRONT.getWidth(), BodyPart.AlexLeftArmSkin.FRONT.getHeight());
        final Bitmap bitmap2 = Bitmap.createBitmap(convertSkin, BodyPart.AlexRightArmSkin.FRONT.getStartX(), BodyPart.AlexRightArmSkin.FRONT.getStartY(), BodyPart.AlexRightArmSkin.FRONT.getWidth(), BodyPart.AlexRightArmSkin.FRONT.getHeight());
        final Bitmap bitmap3 = Bitmap.createBitmap(convertSkin, BodyPart.BodySkin.FRONT.getStartX(), BodyPart.BodySkin.FRONT.getStartY(), BodyPart.BodySkin.FRONT.getWidth(), BodyPart.BodySkin.FRONT.getHeight());
        final Bitmap bitmap4 = Bitmap.createBitmap(convertSkin, BodyPart.LeftLegSkin.FRONT.getStartX(), BodyPart.LeftLegSkin.FRONT.getStartY(), BodyPart.LeftLegSkin.FRONT.getWidth(), BodyPart.LeftLegSkin.FRONT.getHeight());
        final Bitmap bitmap5 = Bitmap.createBitmap(convertSkin, BodyPart.RightLegSkin.FRONT.getStartX(), BodyPart.RightLegSkin.FRONT.getStartY(), BodyPart.RightLegSkin.FRONT.getWidth(), BodyPart.RightLegSkin.FRONT.getHeight());
        final Bitmap bitmap6 = Bitmap.createBitmap(convertSkin, BodyPart.AlexLeftSleeveSkin.FRONT.getStartX(), BodyPart.AlexLeftSleeveSkin.FRONT.getStartY(), BodyPart.AlexLeftSleeveSkin.FRONT.getWidth(), BodyPart.AlexLeftSleeveSkin.FRONT.getHeight());
        final Bitmap bitmap7 = Bitmap.createBitmap(convertSkin, BodyPart.AlexRightSleeveSkin.FRONT.getStartX(), BodyPart.AlexRightSleeveSkin.FRONT.getStartY(), BodyPart.AlexRightSleeveSkin.FRONT.getWidth(), BodyPart.AlexRightSleeveSkin.FRONT.getHeight());
        final Bitmap bitmap8 = Bitmap.createBitmap(convertSkin, BodyPart.JacketSkin.FRONT.getStartX(), BodyPart.JacketSkin.FRONT.getStartY(), BodyPart.JacketSkin.FRONT.getWidth(), BodyPart.JacketSkin.FRONT.getHeight());
        final Bitmap bitmap9 = Bitmap.createBitmap(convertSkin, BodyPart.LeftLegOverlaySkin.FRONT.getStartX(), BodyPart.LeftLegOverlaySkin.FRONT.getStartY(), BodyPart.LeftLegOverlaySkin.FRONT.getWidth(), BodyPart.LeftLegOverlaySkin.FRONT.getHeight());
        final Bitmap bitmap10 = Bitmap.createBitmap(convertSkin, BodyPart.RightLegOverlaySkin.FRONT.getStartX(), BodyPart.RightLegOverlaySkin.FRONT.getStartY(), BodyPart.RightLegOverlaySkin.FRONT.getWidth(), BodyPart.RightLegOverlaySkin.FRONT.getHeight());
        final Bitmap bitmap11 = Bitmap.createBitmap(bitmap4.getWidth() + bitmap5.getWidth() + bitmap3.getWidth(), bitmap.getHeight() + bitmap4.getHeight(), Bitmap.Config.ARGB_8888);
        for (int i = 0; i < bitmap.getWidth(); ++i) {
            for (int j = 0; j < bitmap.getHeight(); ++j) {
                bitmap11.setPixel(i + 1, j, bitmap.getPixel(i, j));
            }
        }
        for (int k = 0; k < bitmap3.getWidth(); ++k) {
            for (int l = 0; l < bitmap3.getHeight(); ++l) {
                bitmap11.setPixel(bitmap.getWidth() + k + 1, l, bitmap3.getPixel(k, l));
            }
        }
        for (int n = 0; n < bitmap2.getWidth(); ++n) {
            for (int n2 = 0; n2 < bitmap2.getHeight(); ++n2) {
                bitmap11.setPixel(bitmap2.getWidth() + n + bitmap3.getWidth() + 1, n2, bitmap2.getPixel(n, n2));
            }
        }
        for (int n3 = 0; n3 < bitmap4.getWidth(); ++n3) {
            for (int n4 = 0; n4 < bitmap4.getHeight(); ++n4) {
                bitmap11.setPixel(bitmap.getWidth() + n3 + 1, bitmap3.getHeight() + n4, bitmap4.getPixel(n3, n4));
            }
        }
        for (int n5 = 0; n5 < bitmap5.getWidth(); ++n5) {
            for (int n6 = 0; n6 < bitmap5.getHeight(); ++n6) {
                bitmap11.setPixel(bitmap.getWidth() + n5 + bitmap4.getWidth() + 1, bitmap3.getHeight() + n6, bitmap5.getPixel(n5, n6));
            }
        }
        for (int n7 = 0; n7 < bitmap6.getWidth(); ++n7) {
            for (int n8 = 0; n8 < bitmap6.getHeight(); ++n8) {
                if (bitmap6.getPixel(n7, n8) != 0) {
                    bitmap11.setPixel(n7 + 1, n8, bitmap6.getPixel(n7, n8));
                }
            }
        }
        for (int n9 = 0; n9 < bitmap8.getWidth(); ++n9) {
            for (int n10 = 0; n10 < bitmap8.getHeight(); ++n10) {
                if (bitmap8.getPixel(n9, n10) != 0) {
                    bitmap11.setPixel(bitmap6.getWidth() + n9 + 1, n10, bitmap8.getPixel(n9, n10));
                }
            }
        }
        for (int n11 = 0; n11 < bitmap7.getWidth(); ++n11) {
            for (int n12 = 0; n12 < bitmap7.getHeight(); ++n12) {
                if (bitmap7.getPixel(n11, n12) != 0) {
                    bitmap11.setPixel(bitmap.getWidth() + n11 + bitmap8.getWidth() + 1, n12, bitmap7.getPixel(n11, n12));
                }
            }
        }
        for (int n13 = 0; n13 < bitmap9.getWidth(); ++n13) {
            for (int n14 = 0; n14 < bitmap9.getHeight(); ++n14) {
                if (bitmap9.getPixel(n13, n14) != 0) {
                    bitmap11.setPixel(bitmap.getWidth() + n13 + 1, bitmap3.getHeight() + n14, bitmap9.getPixel(n13, n14));
                }
            }
        }
        for (int n15 = 0; n15 < bitmap10.getWidth(); ++n15) {
            for (int n16 = 0; n16 < bitmap10.getHeight(); ++n16) {
                if (bitmap10.getPixel(n15, n16) != 0) {
                    bitmap11.setPixel(bitmap.getWidth() + n15 + bitmap4.getWidth() + 1, bitmap3.getHeight() + n16, bitmap10.getPixel(n15, n16));
                }
            }
        }
        bitmap.recycle();
        bitmap2.recycle();
        bitmap3.recycle();
        bitmap4.recycle();
        bitmap5.recycle();
        bitmap6.recycle();
        bitmap7.recycle();
        bitmap8.recycle();
        bitmap9.recycle();
        bitmap10.recycle();
        return bitmap11;
    }
    
    public static Bitmap getHatBackOfSkin(final Bitmap bitmap) {
        final BodyPartSection bodyPartSection = BodyPart.HAT.getBodyPartSection(2);
        return Bitmap.createBitmap(bitmap, bodyPartSection.getStartX(), bodyPartSection.getStartY(), bodyPartSection.getWidth(), bodyPartSection.getHeight());
    }
    
    public static Bitmap getHatFrontOfSkin(final Bitmap bitmap) {
        final BodyPartSection bodyPartSection = BodyPart.HAT.getBodyPartSection(0);
        return Bitmap.createBitmap(bitmap, bodyPartSection.getStartX(), bodyPartSection.getStartY(), bodyPartSection.getWidth(), bodyPartSection.getHeight());
    }
    
    public static Bitmap getHeadBackOfSkin(final Bitmap bitmap) {
        final BodyPartSection bodyPartSection = BodyPart.HEAD.getBodyPartSection(2);
        return Bitmap.createBitmap(bitmap, bodyPartSection.getStartX(), bodyPartSection.getStartY(), bodyPartSection.getWidth(), bodyPartSection.getHeight());
    }
    
    public static Bitmap getHeadFrontOfSkin(final Bitmap bitmap) {
        final BodyPartSection bodyPartSection = BodyPart.HEAD.getBodyPartSection(0);
        return Bitmap.createScaledBitmap(Bitmap.createBitmap(bitmap, bodyPartSection.getStartX(), bodyPartSection.getStartY(), bodyPartSection.getWidth(), bodyPartSection.getHeight()), 128, 128, false);
    }
    
    public static boolean isOldSkin(final Bitmap bitmap) {
        return bitmap.getHeight() < 50;
    }
    
    public static boolean isSteveSkin(final Bitmap bitmap) {
        return bitmap.getPixel(54, 22) != 0;
    }
    
    public static Bitmap overlay(final Bitmap bitmap, final Bitmap bitmap2) {
        final Bitmap bitmap3 = Bitmap.createBitmap(bitmap.getWidth() + bitmap2.getWidth() + 20, bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap3);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint)null);
        canvas.drawBitmap(bitmap2, (float)(bitmap.getWidth() + 20), 0.0f, (Paint)null);
        return bitmap3;
    }
    
    public static Bitmap putBodyAndHeadAlexTogether(Bitmap scaledBitmap, final Bitmap bitmap) {
        scaledBitmap = Bitmap.createScaledBitmap(scaledBitmap, (int)(scaledBitmap.getWidth() * 1.125), (int)(scaledBitmap.getHeight() * 1.125), false);
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), scaledBitmap.getHeight() + bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        for (int i = 0; i < bitmap.getWidth(); ++i) {
            for (int j = 0; j < bitmap.getHeight(); ++j) {
                if (bitmap.getPixel(i, j) != 0) {
                    bitmap2.setPixel(i, scaledBitmap.getHeight() + j, bitmap.getPixel(i, j));
                }
            }
        }
        for (int k = 0; k < scaledBitmap.getWidth(); ++k) {
            for (int l = 0; l < scaledBitmap.getHeight(); ++l) {
                if (scaledBitmap.getPixel(k, l) != 0) {
                    bitmap2.setPixel((bitmap.getWidth() - scaledBitmap.getWidth()) / 2 + k, l + 5, scaledBitmap.getPixel(k, l));
                }
            }
        }
        scaledBitmap.recycle();
        return bitmap2;
    }
    
    public static Bitmap putBodyAndHeadTogether(Bitmap scaledBitmap, final Bitmap bitmap) {
        scaledBitmap = Bitmap.createScaledBitmap(scaledBitmap, (int)(scaledBitmap.getWidth() * 1.125), (int)(scaledBitmap.getHeight() * 1.125), false);
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), scaledBitmap.getHeight() + bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        for (int i = 0; i < bitmap.getWidth(); ++i) {
            for (int j = 0; j < bitmap.getHeight(); ++j) {
                if (bitmap.getPixel(i, j) != 0) {
                    bitmap2.setPixel(i, scaledBitmap.getHeight() + j, bitmap.getPixel(i, j));
                }
            }
        }
        for (int k = 0; k < scaledBitmap.getWidth(); ++k) {
            for (int l = 0; l < scaledBitmap.getHeight(); ++l) {
                if (scaledBitmap.getPixel(k, l) != 0) {
                    bitmap2.setPixel((bitmap.getWidth() - scaledBitmap.getWidth()) / 2 + k, l + 5, scaledBitmap.getPixel(k, l));
                }
            }
        }
        scaledBitmap.recycle();
        return bitmap2;
    }
    
    public static Bitmap renderBackFront(Bitmap bitmap) {
        if (isSteveSkin(bitmap)) {
            final Bitmap bodyBackOfSkin = getBodyBackOfSkin(bitmap);
            final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bodyBackOfSkin, bodyBackOfSkin.getWidth() * 9, bodyBackOfSkin.getHeight() * 9, false);
            bitmap = putBodyAndHeadTogether(renderBackHead(bitmap), scaledBitmap);
            bodyBackOfSkin.recycle();
            scaledBitmap.recycle();
            return bitmap;
        }
        final Bitmap bodyOfAlexSkin = getBodyOfAlexSkin(bitmap);
        final Bitmap scaledBitmap2 = Bitmap.createScaledBitmap(bodyOfAlexSkin, bodyOfAlexSkin.getWidth() * 9, bodyOfAlexSkin.getHeight() * 9, false);
        bitmap = putBodyAndHeadAlexTogether(renderBackHead(bitmap), scaledBitmap2);
        bodyOfAlexSkin.recycle();
        scaledBitmap2.recycle();
        return bitmap;
    }
    
    public static Bitmap renderBackHead(Bitmap scaledBitmap) {
        final Bitmap scaledBitmap2 = Bitmap.createScaledBitmap(getHeadBackOfSkin(scaledBitmap), 64, 64, false);
        scaledBitmap = Bitmap.createScaledBitmap(getHatBackOfSkin(scaledBitmap), 72, 72, false);
        final Bitmap bitmap = Bitmap.createBitmap(72, 72, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < scaledBitmap2.getWidth(); ++i) {
            for (int j = 0; j < scaledBitmap2.getHeight(); ++j) {
                bitmap.setPixel(i + 4, j + 4, scaledBitmap2.getPixel(i, j));
            }
        }
        for (int k = 0; k < scaledBitmap.getWidth(); ++k) {
            for (int l = 0; l < scaledBitmap.getHeight(); ++l) {
                final int pixel = scaledBitmap.getPixel(k, l);
                if (pixel != 0) {
                    bitmap.setPixel(k, l, pixel);
                }
            }
        }
        return bitmap;
    }
    
    public static Bitmap renderBodyFront(Bitmap bitmap) {
        if (isSteveSkin(bitmap)) {
            final Bitmap bodyFrontOfSkin = getBodyFrontOfSkin(bitmap);
            final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bodyFrontOfSkin, bodyFrontOfSkin.getWidth() * 9, bodyFrontOfSkin.getHeight() * 9, false);
            bitmap = putBodyAndHeadTogether(renderFrontHead(bitmap), scaledBitmap);
            bodyFrontOfSkin.recycle();
            scaledBitmap.recycle();
            return bitmap;
        }
        final Bitmap bodyOfAlexSkin = getBodyOfAlexSkin(bitmap);
        final Bitmap scaledBitmap2 = Bitmap.createScaledBitmap(bodyOfAlexSkin, bodyOfAlexSkin.getWidth() * 9, bodyOfAlexSkin.getHeight() * 9, false);
        bitmap = putBodyAndHeadAlexTogether(renderFrontHead(bitmap), scaledBitmap2);
        bodyOfAlexSkin.recycle();
        scaledBitmap2.recycle();
        return bitmap;
    }
    
    public static Bitmap renderFrontHead(Bitmap scaledBitmap) {
        final Bitmap scaledBitmap2 = Bitmap.createScaledBitmap(getHeadFrontOfSkin(scaledBitmap), 64, 64, false);
        scaledBitmap = Bitmap.createScaledBitmap(getHatFrontOfSkin(scaledBitmap), 72, 72, false);
        final Bitmap bitmap = Bitmap.createBitmap(72, 72, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < scaledBitmap2.getWidth(); ++i) {
            for (int j = 0; j < scaledBitmap2.getHeight(); ++j) {
                bitmap.setPixel(i + 4, j + 4, scaledBitmap2.getPixel(i, j));
            }
        }
        for (int k = 0; k < scaledBitmap.getWidth(); ++k) {
            for (int l = 0; l < scaledBitmap.getHeight(); ++l) {
                final int pixel = scaledBitmap.getPixel(k, l);
                if (pixel != 0) {
                    bitmap.setPixel(k, l, pixel);
                }
            }
        }
        return bitmap;
    }
    
    public static Bitmap resizeHead(final Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap, 504, 504, false);
    }
    
    public static Bitmap resizeHead(final Bitmap bitmap, final int n) {
        return Bitmap.createScaledBitmap(bitmap, n, n, false);
    }
}
