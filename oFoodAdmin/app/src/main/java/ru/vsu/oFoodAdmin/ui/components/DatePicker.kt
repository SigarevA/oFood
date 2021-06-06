package ru.vsu.oFoodAdmin.ui.components

import android.util.Log
import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.vsu.oFoodAdmin.R
import java.util.*

@Composable
fun DatePicker(onDateSelected: (Calendar) -> Unit, onDismissRequest: () -> Unit) {
    val selDate = remember { mutableStateOf(Calendar.getInstance()) }
    Dialog(onDismissRequest = { onDismissRequest() }, properties = DialogProperties()) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(size = 16.dp)
                )
        ) {
            Column(
                Modifier
                    .defaultMinSize(minHeight = 72.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.choose_date).toUpperCase(Locale("ru")),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onPrimary
                )
                Spacer(modifier = Modifier.size(18.dp))
            }

            CustomCalendarView(onDateSelected = {
                selDate.value = it
            })

            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "Cancel",
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.onPrimary
            )
            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 16.dp, end = 16.dp)
            ) {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel)
                    )
                }

                TextButton(
                    onClick = {
                        onDateSelected(selDate.value)
                        onDismissRequest()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.confirmation)
                    )
                }

            }
        }
    }
}

@Composable
fun CustomCalendarView(onDateSelected: (Calendar) -> Unit) {
    AndroidView(
        modifier = Modifier.wrapContentSize(),
        factory = { context ->
            CalendarView(context)
        },
        update = { view ->
            view.minDate = Calendar.getInstance().timeInMillis

            view.setOnDateChangeListener { _, year, month, dayOfMonth ->
                Log.d("AndroidView", "year : $year, month : $month, dayOfMonth : $dayOfMonth")
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                onDateSelected(calendar)
            }
        }
    )
}