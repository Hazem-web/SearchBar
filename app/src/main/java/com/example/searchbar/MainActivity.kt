package com.example.searchbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.searchbar.ui.theme.SearchBarTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    val names= listOf("Eslam", "Aya", "Amany", "Ahmed", "Tasneem", "Hazem", "Khairy", "Zeyad", "Suhaila", "Shereen", "Sameh", "Husein", "Khaled", "Mahmoud", "Mayada", "Mariam", "Nada", "Nadia", "Noha")
    val sharedFlow= MutableSharedFlow<List<String>>()
    lateinit var searchText:MutableState<String>
    lateinit var displayedResultsState:MutableState<List<String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            searchText= remember { mutableStateOf("") }
            val scope = rememberCoroutineScope()
            displayedResultsState = remember { mutableStateOf<List<String>>(emptyList()) }

            LaunchedEffect(sharedFlow) {
                sharedFlow.collect { results ->
                    displayedResultsState.value = results
                }
            }

            SearchBarTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TextField(
                            value = searchText.value,
                            onValueChange = {
                                searchText.value=it
                                scope.launch {
                                    if (it.isBlank())
                                        sharedFlow.emit(emptyList())
                                    else{
                                        val filteredNames = names.filter { it.contains(searchText.value, ignoreCase = true) }
                                        sharedFlow.emit(filteredNames)
                                    }

                                }
                            },
                            placeholder = { Text("Search here") }
                        )
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(displayedResultsState.value){
                                Text(it)
                            }
                        }
                    }
                }
            }
        }
    }
}
