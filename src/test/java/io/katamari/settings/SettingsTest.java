package io.katamari.settings;

import io.katamari.settings.types.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SettingsTest {

  private Settings settings;

  @Before
  public void loadSettings() {
    settings = Settings.load(getClass().getResourceAsStream("/yaml/test-1.yaml"));
  }

  @Test
  public void testBasics() {
    assertThat(settings.getAsString("foo", "defaultValue"), is("bar"));
    assertThat(settings.getAsString("foo2", "defaultValue"), is("defaultValue"));
    assertThat(settings.getAsString("l2.l31"), is("key"));
    assertThat(settings.getAsString("l2.l32"), is("value"));

    assertThat(settings.componentSettings("l2").getAsString("l31"), is("key"));
    assertThat(settings.componentSettings("l2").getAsString("l32"), is("value"));
  }

  @Test
  public void testNumberFunctionsWork() {
    assertThat(settings.componentSettings("numbers").getAsFloat("float", 0.0f), is(2.65f));
    assertThat(settings.componentSettings("numbers").getAsDouble("double", 0.0), is(2.67));
    assertThat(settings.componentSettings("numbers").getAsInt("integer", 0), is(2432));
    assertThat(settings.componentSettings("numbers").getAsLong("long", 0L), is(2435453453L));
  }

  @Test
  public void testTimeValuesWork() {
    TimeValue expectedTimeValueMinutes = TimeValue.timeValueMinutes(5);
    TimeValue readTimeValue = settings.componentSettings("time").getAsTime("minutes", TimeValue.timeValueHours(1));
    assertThat(readTimeValue.toString(), is(expectedTimeValueMinutes.toString()));
    TimeValue expectedTimeValueDays = TimeValue.timeValueHours(240);
    TimeValue readTimeValueDays = settings.componentSettings("time").getAsTime("days", TimeValue.timeValueHours(1));
    assertThat(readTimeValueDays.toString(), is(expectedTimeValueDays.toString()));
  }

  @Test
  public void testBooleanFunctionsWork() {
    assertThat(settings.componentSettings("boolean").getAsBoolean("shouldBeOn", false), is(true));
    assertThat(settings.componentSettings("boolean").getAsBoolean("shouldBeTrue", false), is(true));
    assertThat(settings.componentSettings("boolean").getAsBoolean("shouldBeYes", false), is(true));
    assertThat(settings.componentSettings("boolean").getAsBoolean("shouldBe1", false), is(true));
    assertThat(settings.componentSettings("boolean").getAsBoolean("shouldBeOff", true), is(false));
    assertThat(settings.componentSettings("boolean").getAsBoolean("shouldBeFalse", true), is(false));
    assertThat(settings.componentSettings("boolean").getAsBoolean("shouldBeNo", true), is(false));
    assertThat(settings.componentSettings("boolean").getAsBoolean("shouldBe0", true), is(false));
  }

  @Test
  public void testByteSizeValuesWork() {
    ByteSizeValue byteSizeValue = new ByteSizeValue(24, ByteSizeUnit.MB);
    assertThat(settings.componentSettings("bytes").getAsBytesSize("some", null).bytes(), is(byteSizeValue.bytes()));
  }

  @Test
  public void testSizeValuesWork() {
    SizeValue sizeValue = new SizeValue(100, SizeUnit.KILO);
    assertThat(settings.componentSettings("sizes").getAsSize("some", null).toString(), is(sizeValue.toString()));
  }

  @Test
  public void testThatBuilderWorks() {
    Settings settings = Settings.builder().put("foo.bar.baz", "on").build();
    assertThat(settings.componentSettings("foo").componentSettings("bar").getAsBoolean("baz", false), is(true));

  }

  @Test
  public void testThatLoadingFromFileWorks() {
    Settings fileSettings = Settings.load(new File("src/test/resources/yaml/test-1.yaml"));
    assertThat(fileSettings.componentSettings("boolean").getAsBoolean("shouldBe0", true), is(false));
    Settings stringFileSettings = Settings.load("src/test/resources/yaml/test-1.yaml");
    assertThat(stringFileSettings.componentSettings("boolean").getAsBoolean("shouldBe0", true), is(false));
  }
}
