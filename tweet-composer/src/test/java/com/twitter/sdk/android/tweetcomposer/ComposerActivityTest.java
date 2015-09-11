/*
 * Copyright (C) 2015 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.twitter.sdk.android.tweetcomposer;

import android.content.Context;
import android.content.Intent;

import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ComposerActivityTest {
    private static final String TWEET_TEXT = "tweet text";
    private Context mockContext;
    private TwitterSession mockSession;
    private TwitterAuthToken mockAuthToken;
    private Card mockCardData;

    @Before
    public void setUp() {
        mockContext = mock(Context.class);
        mockSession = mock(TwitterSession.class);
        mockAuthToken = mock(TwitterAuthToken.class);
        mockCardData = mock(Card.class);
        when(mockSession.getAuthToken()).thenReturn(mockAuthToken);
    }

    @Test
    public void testBuilder() {
        final ComposerActivity.Builder builder = new ComposerActivity.Builder(mockContext);
        assertNotNull(builder);
    }

    @Test
    public void testBuilder_nullContext() {
        try {
            new ComposerActivity.Builder(null);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Context must not be null", e.getMessage());
        }
    }

    @Test
    public void testBuilderSession() {
        final Intent intent = new ComposerActivity.Builder(mockContext)
                .session(mockSession)
                .createIntent();
        verify(mockSession).getAuthToken();
        assertEquals(mockAuthToken, intent.getParcelableExtra(ComposerActivity.EXTRA_USER_TOKEN));
    }

    @Test
    public void testBuilderSession_nullSession() {
        try {
            new ComposerActivity.Builder(mockContext).session(null);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("TwitterSession must not be null", e.getMessage());
        }
    }

    @Test
    public void testBuilderSession_nullAuthToken() {
        when(mockSession.getAuthToken()).thenReturn(null);
        try {
            new ComposerActivity.Builder(mockContext).session(mockSession);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("TwitterSession token must not be null", e.getMessage());
        }
    }

    @Test
    public void testBuilderSession_sessionNotSet() {
        try {
            new ComposerActivity.Builder(mockContext).createIntent();
        } catch (IllegalStateException e) {
            assertEquals("Must set a TwitterSession", e.getMessage());
        }
    }

    @Test
    public void testBuilderTweetText() {
        final Intent intent = new ComposerActivity.Builder(mockContext)
                .session(mockSession)
                .tweetText(TWEET_TEXT)
                .createIntent();
        assertEquals(TWEET_TEXT, intent.getStringExtra(ComposerActivity.EXTRA_TWEET_TEXT));
    }


    @Test
    public void testBuilderCardData() {
        final Intent intent = new ComposerActivity.Builder(mockContext)
                .session(mockSession)
                .card(mockCardData)
                .createIntent();
        assertEquals(mockCardData, intent.getSerializableExtra(ComposerActivity.EXTRA_CARD_DATA));
    }
}
