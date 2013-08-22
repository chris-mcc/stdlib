package com.mediasmiths.std.types;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TimecodeTest
{
	@Test
	public void testResample_IdentityFunction_Zero()
	{
		final Timecode src = Timecode.getInstance("00:00:00:00", Framerate.HZ_25);
		final Timecode dst = src.resample(Framerate.HZ_25);

		assertEquals(src.toString(), dst.toString());
	}


	@Test
	public void testResample_IdentityFunction()
	{
		final Timecode src = Timecode.getInstance("01:02:03:04", Framerate.HZ_25);
		final Timecode dst = src.resample(Framerate.HZ_25);

		assertEquals(src.toString(), dst.toString());
	}


	@Test
	public void testResample()
	{
		final Timecode src = Timecode.getInstance("09:08:07:04", Framerate.HZ_50);
		final Timecode dst = src.resample(Framerate.HZ_25);

		assertEquals("09:08:07:02", dst.toString());
	}


	@Test
	public void testBounds()
	{
		final Timecode small = Timecode.getInstance("00:00:00:00", Framerate.HZ_50);
		final Timecode big = Timecode.getInstance("00:00:00:01", Framerate.HZ_50);

		assertTrue(small.lt(big));
		assertTrue(big.gt(small));
	}


	@Test
	public void testResamplePrecise_HappyPath()
	{
		final Timecode src = Timecode.getInstance("09:08:07:02", Framerate.HZ_50);
		final Timecode dst = src.resample(Framerate.HZ_25);

		assertEquals("09:08:07:01", dst.toString());
	}


	@Test
	public void testResamplePrecise_HappyPath_Zero()
	{
		final Timecode src = Timecode.getInstance("00:00:00:00", Framerate.HZ_50);
		final Timecode dst = src.resample(Framerate.HZ_25);

		assertEquals("00:00:00:00", dst.toString());
	}


	@Test(expected = ResamplingException.class)
	public void testResamplePrecise_Imprecise() throws Exception
	{
		final Timecode src = Timecode.getInstance("09:08:07:03", Framerate.HZ_50);

		src.resamplePrecise(Framerate.HZ_25);
	}


	@Test
	public void testResample_Imprecise()
	{
		final Timecode src = Timecode.getInstance("09:08:07:03", Framerate.HZ_50);
		final Timecode dst = src.resample(Framerate.HZ_25);

		assertEquals("09:08:07:02", dst.toString());
	}


	@Test
	public void testGetSampleCountDelta_Zero()
	{
		final Timecode base = Timecode.getInstance("00:00:00:00", Framerate.HZ_50);
		final Timecode tc = Timecode.getInstance("00:00:00:00", Framerate.HZ_25);

		final SampleCount delta = tc.getSampleCount(base);

		assertEquals(0, delta.getSamples());
		assertEquals(0, tc.getSampleCount().getSamples());
	}


	@Test
	public void testGetSampleCountDelta()
	{
		// 2 timecodes which are 1h2m3s and 4 frames apart
		final Timecode base = Timecode.getInstance("01:02:03:04", Framerate.HZ_50);
		final Timecode tc = Timecode.getInstance("02:04:06:08", Framerate.HZ_50);

		final SampleCount delta = tc.getSampleCount(base);
		final Timecode deltaTC = Timecode.getInstance(delta);

		assertEquals("01:02:03:04", deltaTC.toString());
	}


	@Test
	public void testDurationInSeconds()
	{
		final Timecode base = Timecode.getInstance("01:02:03:49", Framerate.HZ_50);

		assertEquals(3723, base.getDurationInSeconds());
	}


	/**
	 * Test that -00:00:00:02 is parsed and correctly and when encoded to samples returns the right number of samples
	 */
	@Test
	public void testSampleCountForNegativeTimecode()
	{
		final Timecode timecode = Timecode.getInstance("-00:00:00:02@25");
		SampleCount samples = new SampleCount(-2, Framerate.HZ_25);

		assertEquals(samples, timecode.getSampleCount());
	}


	@Test
	public void testGetInstanceWithNegativeSampleCount()
	{
		final Timecode tc = Timecode.getInstance(new SampleCount(-2, Framerate.HZ_25), false);

		assertEquals("-00:00:00:02@25", tc.toMediasmithsString());
	}


	@Test
	public void testAddNegativeSamples()
	{
		final SampleCount samples = new SampleCount(-2, Framerate.HZ_25);

		final Timecode base = Timecode.getInstance("-00:00:00:02@25");

		final Timecode tc = base.add(samples);

		assertEquals("-00:00:00:04@25", tc.toMediasmithsString());
	}


	/**
	 * The SMPTE string shouldn't include days
	 */
	@Test
	public void testAddSmpte()
	{
		final Timecode base = Timecode.getInstance("23:29:59:05", Framerate.HZ_25);
		final Timecode add = Timecode.getInstance("00:59:59:22", Framerate.HZ_25);
		final Timecode tc = base.add(add.getSampleCount());

		Assert.assertEquals("00:29:59:02", tc.toSMPTEString());
	}


	/**
	 * The SMPTE string including days should include days
	 */
	@Test
	public void testAddSmpteIncludingDays()
	{
		final Timecode base = Timecode.getInstance("23:29:59:05", Framerate.HZ_25);
		final Timecode add = Timecode.getInstance("00:59:59:22", Framerate.HZ_25);
		final Timecode tc = base.add(add.getSampleCount());

		Assert.assertEquals("01:00:29:59:02", tc.toSMPTEString(true));
	}


	@Test
	public void testSubtractNegativeSamples()
	{
		final SampleCount samples = new SampleCount(-2, Framerate.HZ_25);

		final Timecode base = Timecode.getInstance("-00:00:00:02@25");

		final Timecode tc = base.subtract(samples);

		assertEquals("00:00:00:00@25", tc.toMediasmithsString());
	}


	/**
	 * Not a valid timecode representation (frame count is >= 1 second at this timebase)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testDenormalisedFrameField()
	{
		Timecode.getInstance("00:00:00:25@25"); // frames >= timebase
	}


	/**
	 * Negative zero timecode should be converted to positive zero
	 */
	@Test
	public void testNegativeZeroTimecode()
	{
		final Timecode tc = Timecode.getInstance("-00:00:00:00@25");

		assertEquals("00:00:00:00@25", tc.toMediasmithsString());
	}


	/**
	 * Timecode eq should be based on the instant in time it refers to
	 */
	@Test
	public void testEq()
	{
		final Timecode a = Timecode.getInstance("01:02:03:04@25");
		final Timecode b = Timecode.getInstance("01:02:03:08@50");

		assertTrue(a.eq(b));
	}


	/**
	 * Negative zero timecode should be converted to positive zero
	 */
	@Test
	public void testBetween()
	{
		final Timecode start = Timecode.getInstance("-01:02:03:04@25");
		final Timecode end = Timecode.getInstance("03:02:03:04@25");

		final List<String> yes = Arrays.asList("-01:02:03:04",
		                                       "00:00:00:00",
		                                       "01:00:00:00",
		                                       "02:00:00:00",
		                                       "03:00:00:00",
		                                       "03:02:03:03",
		                                       "03:02:03:04");
		final List<String> no = Arrays.asList("-01:02:03:05", "03:02:03:05", "-02:03:04:05", "04:00:00:00");

		for (String smpte : yes)
		{
			final Timecode tc = Timecode.getInstance(smpte, Framerate.HZ_25);

			final boolean between = tc.between(start, end);

			assertTrue(smpte + " should be between " + start + " and " + end, between);
		}

		for (String smpte : no)
		{
			final Timecode tc = Timecode.getInstance(smpte, Framerate.HZ_25);

			final boolean between = tc.between(start, end);

			assertFalse(smpte + " shouldn't be between " + start + " and " + end, between);
		}
	}
}
