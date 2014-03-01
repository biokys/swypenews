package com.droid4you.application.swypenews;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import com.robotium.solo.Solo;
import junit.framework.Assert;


/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.droid4you.application.swypenews.MainActivityTest \
 * com.droid4you.application.swypenews.tests/android.test.InstrumentationTestRunner
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;
    public MainActivityTest() {
        super("com.droid4you.application.swypenews", MainActivity.class);
    }
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }
    @SmallTest
    public void testOpenMenu() throws Exception { //Open settings and that's all
        solo.waitForActivity(solo.getCurrentActivity().toString());
        solo.sendKey(Solo.MENU);
      //  solo.waitForText("Nastavení");
        Context context = this.getInstrumentation().getTargetContext().getApplicationContext();
        solo.clickOnMenuItem(context.getResources().getString(R.string.menu_config));
        Assert.assertTrue(solo.waitForText(context.getResources().getString(R.string.select_countries)));
        if (solo.isTextChecked("Great Britain")) {
            solo.clickOnText("Great Britain");
            Assert.assertTrue(!solo.isTextChecked("Great Britain"));
        }
        else {
            solo.clickOnText("Great Britain");
            Assert.assertTrue(solo.isTextChecked("Great Britain"));
        }
        solo.clickOnText("Ok");

    }

/*
    public void testSwyping() throws Exception {
        PointF x1 = new PointF();
        PointF x2 = new PointF();
        PointF x3 = new PointF();
        PointF x4 = new PointF();
        x1.set(10.0f, 10.0f);
        x2 = x1;
        x3.set(100.0f,10.0f);
        x4=x3;

        solo.swipe(x1,x2,x3,x4);
        Assert.assertTrue(true);
    //TODO: INJECT_EVENTS permission???
    }
*/
    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
