package com.hanmajid.android.seed.ui.connectivity.wifi.p2p

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.hanmajid.android.seed.databinding.FragmentWifiP2pBinding
import com.hanmajid.android.seed.util.PermissionUtil
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.and
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

@AndroidEntryPoint
class WifiP2pFragment : Fragment() {
    private val viewModel: WifiP2pViewModel by viewModels()

    private lateinit var binding: FragmentWifiP2pBinding

    private val locationManager: LocationManager by lazy(LazyThreadSafetyMode.NONE) {
        requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val wifiManager: WifiManager by lazy(LazyThreadSafetyMode.NONE) {
        requireContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    private val wifiP2pManager: WifiP2pManager by lazy(LazyThreadSafetyMode.NONE) {
        requireContext().getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    }
    private var wifiP2pChannel: WifiP2pManager.Channel? = null
    private var isWifiP2pEnabled: Boolean = false
    private var host: String? = ""
    private var isGroupOwner: Boolean = false

    private lateinit var adapter: WifiP2PDeviceListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWifiP2pBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Listen to Wi-Fi state changes here.
//        WifiStateBroadcastListener(
//            viewLifecycleOwner,
//            requireContext(),
//            { _, _ ->
//                doRefresh()
//            }, {
//
//            }
//        )

        wifiP2pChannel = wifiP2pManager.initialize(requireContext(), Looper.getMainLooper(), null)
        wifiP2pChannel?.also { channel ->
            WifiP2pBroadcastReceiver(
                wifiP2pManager,
                channel,
                requireActivity(),
                viewLifecycleOwner,
                { state ->
                    Log.wtf(TAG, "P2P state changed")
                    isWifiP2pEnabled = state == WifiP2pManager.WIFI_P2P_STATE_ENABLED
                    refreshWifiState()
                },
                {
                    Log.wtf(TAG, it.deviceList.toString())
                    adapter.submitList(it.deviceList.toList())
                    binding.isLoading = false
                },
                { info, group ->
                    group?.clientList?.forEach {
                        Log.wtf("CLIENT", it.toString())
                    }
                    info?.let { info ->
                        if (info.groupFormed) {
                            host = info.groupOwnerAddress.hostAddress
                            isGroupOwner = info.isGroupOwner
                            binding.textGroupOwner.text =
                                if (isGroupOwner) "Group Owner: Yes" else "Group Owner: No"
                            binding.textMyIp.text = getDottedDecimalIP(getLocalIPAddress()!!)!!
                            if (isGroupOwner) {
                                Snackbar.make(
                                    binding.root,
                                    "Group Owner!!!",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
//                            viewModel.startServer()
                        }
                    }
                }
            )
        }

        setupBinding()
        tryRefresh()
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        adapter = WifiP2PDeviceListAdapter(
            requireContext(),
            wifiP2pManager,
            wifiP2pChannel,
            {
                // Allow user to pick an image from Gallery or other
                // registered apps
                val intentLauncher =
                    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                        val uri = it.data?.data
                        host?.apply {
                            uri?.let {
                                val clientHost =
                                    if (isGroupOwner) getDottedDecimalIP(getLocalIPAddress()!!)!! else this
                                val clientPort =
                                    if (isGroupOwner) WifiP2pViewModel.NON_GROUP_OWNER_PORT else WifiP2pViewModel.GROUP_OWNER_PORT
                                viewModel.sendFile(clientHost, clientPort, it) { message ->
                                    message?.let {
                                        Snackbar.make(
                                            binding.root,
                                            it,
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                    }
                intentLauncher.launch(
                    Intent(Intent.ACTION_GET_CONTENT).apply {
                        type = "image/*"
                    }
                )
            },
            {
                viewModel.startServer(isGroupOwner) { message ->
                    message?.let {
                        Snackbar.make(
                            binding.root,
                            it,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
                val dialog = MaterialAlertDialogBuilder(requireContext())
                    .setTitle(
                        if (isGroupOwner) "Group Owner" else "Not Group Owner"
                    )
                    .setMessage("Waiting for files...")
                    .show()
            })
        binding.recyclerView.adapter = adapter
        binding.isLoading = true

        binding.swipeRefresh.setOnRefreshListener {
            tryRefresh()
        }

        val intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                tryRefresh()
            }
        binding.buttonWifiSettings.setOnClickListener {
            intentLauncher.launch(
                Intent(
                    Settings.ACTION_WIFI_SETTINGS
                )
            )
        }
        binding.buttonLocationSettings.setOnClickListener {
            intentLauncher.launch(
                Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS
                )
            )
        }
        binding.buttonPermissionSettings.setOnClickListener {
            intentLauncher.launch(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", requireActivity().packageName, null)
                }
            )
        }
    }

    private fun tryRefresh() {
        binding.isLoading = true
        binding.isPermissionGranted = true
        if (PermissionUtil.allPermissionsGranted(
                requireContext(),
                REQUIRED_PERMISSIONS
            )
        ) {
            doRefresh()
        } else {
            PermissionUtil.requestAllPermissions(
                this,
                binding.root,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun refreshWifiState(): Boolean {
        val isWifiEnabled = wifiManager.wifiState == WifiManager.WIFI_STATE_ENABLED
        binding.isWifiEnabled = isWifiEnabled && isWifiP2pEnabled
        Log.wtf(TAG, isWifiEnabled.toString())
        return isWifiEnabled
    }

    private fun doRefresh() {
        binding.isLoading = true

        val isWifiEnabled = refreshWifiState()

        val isLocationEnabled = LocationManagerCompat.isLocationEnabled(locationManager)
        binding.isLocationEnabled = isLocationEnabled

        if (isLocationEnabled && isWifiEnabled) {
            adapter.submitList(emptyList())
            Log.wtf(TAG, "Discovering....")
            wifiP2pManager.discoverPeers(
                wifiP2pChannel,
                object : WifiP2pManager.ActionListener {
                    override fun onSuccess() {
                        viewModel.isInitialized = true
                    }

                    override fun onFailure(reason: Int) {
                        binding.isLoading = false
                    }
                })
        } else {
            binding.isLoading = false
            viewModel.isInitialized = false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (PermissionUtil.allPermissionsGranted(
                    requireContext(),
                    REQUIRED_PERMISSIONS
                )
            ) {
                doRefresh()
            } else {
                binding.isLoading = false
                binding.isPermissionGranted = false
            }
        }
    }

    private fun getLocalIPAddress(): ByteArray? {
        try {
            val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf: NetworkInterface = en.nextElement()
                val enumIpAddr: Enumeration<InetAddress> = intf.getInetAddresses()
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress: InetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress()) {
                        if (inetAddress is Inet4Address) { // fix for Galaxy Nexus. IPv4 is easy to use :-)
                            return inetAddress.getAddress()
                        }
                        //return inetAddress.getHostAddress().toString(); // Galaxy Nexus returns IPv6
                    }
                }
            }
        } catch (ex: SocketException) {
            //Log.e("AndroidNetworkAddressFactory", "getLocalIPAddress()", ex);
        } catch (ex: NullPointerException) {
            //Log.e("AndroidNetworkAddressFactory", "getLocalIPAddress()", ex);
        }
        return null
    }

    private fun getDottedDecimalIP(ipAddr: ByteArray): String? {
        //convert to dotted decimal notation:
        var ipAddrStr: String? = ""
        for (i in ipAddr.indices) {
            if (i > 0) {
                ipAddrStr += "."
            }
            ipAddrStr += ipAddr[i] and 0xFF
        }
        return ipAddrStr
    }

    companion object {
        private const val TAG = "WifiP2pFragment"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }
}