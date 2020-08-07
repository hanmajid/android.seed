package com.hanmajid.android.seed.ui.explore

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hanmajid.android.seed.R
import com.hanmajid.android.seed.databinding.FragmentExploreBinding
import com.hanmajid.android.seed.util.IntentUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : Fragment() {

    private lateinit var binding: FragmentExploreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.cameraButton.setOnClickListener {
            findNavController().navigate(
                ExploreFragmentDirections.actionGlobalCameraFragment()
            )
        }

        binding.webviewButton.setOnClickListener {
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreFragmentToUserGuideActivity()
            )
        }

        binding.connectivityButton.setOnClickListener {
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreFragmentToConnectivityNavGraph()
            )
        }

        // TODO: Security > Show an app chooser
        binding.shareButton.setOnClickListener {
            val callIntent: Intent = Uri.parse("tel:5551234").let { number ->
                Intent(Intent.ACTION_DIAL, number)
            }
            val mapIntent: Intent = Uri.parse(
                "geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California"
            ).let { location ->
                // Or map point based on latitude/longitude
                // Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
                Intent(Intent.ACTION_VIEW, location)
            }
            val webIntent: Intent = Uri.parse("https://www.android.com").let { webpage ->
                Intent(Intent.ACTION_VIEW, webpage)
            }

            val intent = webIntent

            IntentUtil.startImplicitIntent(
                activity = requireActivity(),
                intent = intent,
                chooserTitle = getString(R.string.share_text_chooser_title)
            )
        }

        // messagePorts[0] and messagePorts[1] represent the two ports.
        // They are already tangled to each other and have been started.
//        val channel = binding.webview.createWebMessageChannel()
//
//        // Create handler for channel[0] to receive messages.
//        channel[0].setWebMessageCallback(object : WebMessagePort.WebMessageCallback() {
//            override fun onMessage(port: WebMessagePort?, message: WebMessage?) {
//                Log.wtf(TAG, "On port $port, received this message: $message")
//            }
//        })
//        // Send a message from channel[1] to channel[0]
//        channel[1].postMessage(WebMessage("My secure message"))

        // App needs 10 MB within internal storage.
//        val NUM_BYTES_NEEDED_FOR_MY_APP = 1024 * 1024 * 10L
//
//        if (InternalStorageUtil.isStorageAvailable(requireContext(), NUM_BYTES_NEEDED_FOR_MY_APP)) {
//            InternalStorageUtil.allocateStorage(requireContext(), NUM_BYTES_NEEDED_FOR_MY_APP)
//        } else {
//            val storageIntent = Intent().apply {
//                action = ACTION_MANAGE_STORAGE
//            }
//            // TODO: Show dialog here explaining to user that they need to clear some space.
//            startActivity(storageIntent)
//        }

//        val dirName = "images"
//        val dir = requireContext().getDir(dirName, Context.MODE_PRIVATE)
//
//        val files: Array<String> = requireContext().fileList()
//        Log.wtf(TAG, files.joinToString(" | "))

//        val filename = "myfile2.txt"
//        val fileContents = "Hello world\n" +
//                "Hello misery me"
//        InternalStorageUtil.storeFile(requireContext(), filename, fileContents)
//        val x = InternalStorageUtil.readFile(requireContext(), "filename")
//        Log.wtf(TAG, InternalStorageUtil.getStorageInformation(requireContext()).toString())
//        Log.wtf(TAG, x ?: "null")
//        Log.wtf(TAG, InternalStorageUtil.listFiles(requireContext()).joinToString(", "))
//
//        InternalStorageUtil.storeCache(requireContext(), "cache1.txt", "Hello from cache")
//        Log.wtf(TAG, InternalStorageUtil.readCache(requireContext(), "cache1.txt"))
    }

    companion object {
        const val TAG = "ExploreFragment"
    }
}