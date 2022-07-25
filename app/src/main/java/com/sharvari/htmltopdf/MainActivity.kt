package com.sharvari.htmltopdf

import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import com.sharvari.htmltopdf.ui.theme.HTMLtoPDFTheme

class MainViewModel: ViewModel(){
    val url = "https://lgdev5.lgseniorscare.com/login"
    lateinit var pdfDocumentAdapter : PrintDocumentAdapter
}

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HTMLtoPDFTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    Scaffold(
                        topBar = {
                            MyTopAppBar(
                                context = this,
                                viewModel = viewModel
                            )
                        }
                    ) { contentPadding ->
                        // Screen content
                       MyWebView(
                           context = this,
                           viewModel = viewModel
                       )
                    }
                }
            }
        }
    }
}


@Composable
fun MyTopAppBar(
    context: Context,
    viewModel: MainViewModel
){
    TopAppBar(
        title = { Text(text = "HTMLtoPDF", color = Color.White) },
        modifier = Modifier.fillMaxWidth(),
        actions = {
            IconButton(
                onClick = {
//                    Toast.makeText(context, "Print", Toast.LENGTH_SHORT).show()
                    assert(viewModel.pdfDocumentAdapter != null)
                    val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
                    val printJob = printManager.print(viewModel.url, viewModel.pdfDocumentAdapter, PrintAttributes.Builder().build())
                }
            ) {
                Icon(Icons.Filled.Print, "print")
            }
            IconButton(
                onClick = {
                    Toast.makeText(context, "share", Toast.LENGTH_SHORT).show()
                }
            ) {
                Icon(Icons.Filled.Share, "share")
            }
        }
    )
}

@Composable
fun MyWebView(
    context: Context,
    viewModel: MainViewModel
) {
    AndroidView(factory = {
        WebView(context).apply{
            webViewClient = WebViewClient()
            viewModel.pdfDocumentAdapter = createPrintDocumentAdapter(viewModel.url)


            loadUrl(viewModel.url)
        }
    }, update = {
        it.loadUrl(viewModel.url)
    })
}


@Preview(showBackground = true, widthDp = 720, heightDp = 1024)
@Composable
fun DefaultPreview() {
    HTMLtoPDFTheme {
        MyWebView(context = LocalContext.current, viewModel = MainViewModel())
    }
}