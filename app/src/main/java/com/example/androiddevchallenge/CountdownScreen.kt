/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Pause
import androidx.compose.material.icons.sharp.PlayArrow
import androidx.compose.material.icons.sharp.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.MyTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@ExperimentalTime
@Composable
fun CountdownScreen(modifier: Modifier) {
    var duration by remember { mutableStateOf(65.toDuration(DurationUnit.SECONDS)) }
    var playPauseState by rememberSaveable { mutableStateOf(PlayPauseState.PLAY) }

    LaunchedEffect(playPauseState) {
        while (playPauseState == PlayPauseState.PAUSE && duration.inSeconds >= 0) {
            if (duration == Duration.ZERO) {
                playPauseState = PlayPauseState.PLAY
            } else {
                duration -= 1.toDuration(DurationUnit.SECONDS)
            }
            delay(1000)
        }
    }

    Surface(modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimeText(
                durationText = duration.inMinutes.toInt().toString(),
                unitText = "MIN",
                modifier = Modifier.weight(1f)
            )
            TimeText(
                durationText = (duration.inSeconds % 60).toInt().toString(),
                unitText = "SEC",
                modifier = Modifier.weight(1f)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TimeButton(
                    onClick = {
                        duration =
                            (duration - 5.toDuration(DurationUnit.SECONDS)).coerceAtLeast(Duration.ZERO)
                    },
                    imageVector = Icons.Sharp.Remove,
                    contentDescription = "Remove 5 seconds"
                )

                PlayPauseButton(
                    state = playPauseState,
                    onClick = {
                        playPauseState =
                            if (playPauseState == PlayPauseState.PLAY) PlayPauseState.PAUSE else PlayPauseState.PLAY
                    }
                )

                TimeButton(
                    onClick = { duration = duration + 5.toDuration(DurationUnit.SECONDS) },
                    imageVector = Icons.Sharp.Add,
                    contentDescription = "Add 5 seconds"
                )
            }
        }
    }
}

@Composable
fun TimeText(durationText: String, unitText: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = durationText,
            maxLines = 1,
            style = MaterialTheme.typography.h1.copy()
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = unitText,
            maxLines = 1,
            style = MaterialTheme.typography.h6
        )
    }
}

@Preview
@Composable
fun TimeTextPreview() {
    MyTheme {
        TimeText(durationText = "13", unitText = "SCS")
    }
}

enum class PlayPauseState { PLAY, PAUSE }

@Composable
fun PlayPauseButton(
    state: PlayPauseState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color by animateColorAsState(
        if (state == PlayPauseState.PLAY) {
            Color(0xFF57B5ED)
        } else {
            Color(0xFFAFD14E)
        }
    )
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = color),
        shape = RoundedCornerShape(50),
        modifier = modifier.size(72.dp)
    ) {
        when (state) {
            PlayPauseState.PLAY -> {
                Icon(
                    imageVector = Icons.Sharp.PlayArrow,
                    contentDescription = "Launch Countdown",
                    modifier = Modifier.fillMaxSize()
                )
            }
            PlayPauseState.PAUSE -> {
                Icon(
                    imageVector = Icons.Sharp.Pause,
                    contentDescription = "Pause Countdown",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Preview
@Composable
fun PlayPauseLightPreview() {
    MyTheme {
        PlayPauseButton(
            state = PlayPauseState.PLAY,
            onClick = { }
        )
    }
}

@Preview
@Composable
fun PlayPauseDarkPreview() {
    MyTheme(darkTheme = true) {
        PlayPauseButton(
            state = PlayPauseState.PLAY,
            onClick = { }
        )
    }
}

@Composable
fun TimeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    contentDescription: String?
) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Preview
@Composable
fun TimeButtonLightPreview() {
    MyTheme {
        TimeButton(
            onClick = { },
            imageVector = Icons.Rounded.Add,
            contentDescription = "Add 5 seconds"
        )
    }
}

@Preview
@Composable
fun TimeButtonDarkPreview() {
    MyTheme(darkTheme = true) {
        TimeButton(
            onClick = { },
            imageVector = Icons.Rounded.Add,
            contentDescription = "Add 5 seconds"
        )
    }
}
