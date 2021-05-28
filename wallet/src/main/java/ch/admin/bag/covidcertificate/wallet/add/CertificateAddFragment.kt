/*
 * Copyright (c) 2021 Ubique Innovation AG <https://www.ubique.ch>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * SPDX-License-Identifier: MPL-2.0
 */

package ch.admin.bag.covidcertificate.wallet.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ch.admin.bag.covidcertificate.common.util.DEFAULT_DISPLAY_DATE_FORMATTER
import ch.admin.bag.covidcertificate.common.util.parseIsoTimeAndFormat
import ch.admin.bag.covidcertificate.eval.models.Bagdgc
import ch.admin.bag.covidcertificate.wallet.CertificatesViewModel
import ch.admin.bag.covidcertificate.wallet.R
import ch.admin.bag.covidcertificate.wallet.databinding.FragmentCertificateAddBinding
import ch.admin.bag.covidcertificate.wallet.detail.CertificateDetailAdapter
import ch.admin.bag.covidcertificate.wallet.detail.CertificateDetailItemListBuilder

class CertificateAddFragment : Fragment() {

	companion object {
		private const val ARG_CERTIFICATE = "ARG_CERTIFICATE"

		fun newInstance(certificate: Bagdgc): CertificateAddFragment = CertificateAddFragment().apply {
			arguments = bundleOf(ARG_CERTIFICATE to certificate)
		}
	}

	private val certificatesViewModel by activityViewModels<CertificatesViewModel>()

	private var _binding: FragmentCertificateAddBinding? = null
	private val binding get() = _binding!!

	private lateinit var certificate: Bagdgc
	private var isAlreadyAdded = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		certificate = (arguments?.getSerializable(ARG_CERTIFICATE) as? Bagdgc)
			?: throw IllegalStateException("Certificate detail fragment created without Certificate!")
		isAlreadyAdded = certificatesViewModel.containsCertificate(certificate.qrCodeData)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentCertificateAddBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		setupCertificateDetails()

		binding.certificateAddToolbar.setNavigationOnClickListener {
			parentFragmentManager.popBackStack()
		}
		binding.certificateAlreadyExistsInfo.isVisible = isAlreadyAdded
		binding.certificateAddButton.apply {
			if (isAlreadyAdded) {
				text = context.getString(R.string.ok_button)
				setOnClickListener {
					parentFragmentManager.popBackStack()
					parentFragmentManager.popBackStack()
				}
			} else {
				text = context.getString(R.string.wallet_add_certificate)
				setOnClickListener {
					certificatesViewModel.addCertificate(certificate.qrCodeData)
					parentFragmentManager.popBackStack()
					parentFragmentManager.popBackStack()
				}
			}
		}
		binding.certificateAddRetry.setOnClickListener {
			parentFragmentManager.popBackStack()
		}

	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	private fun setupCertificateDetails() {
		val recyclerView = binding.certificateAddDataRecyclerView
		val layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
		recyclerView.layoutManager = layoutManager
		val adapter = CertificateDetailAdapter()
		recyclerView.adapter = adapter

		val name = "${certificate.dgc.nam.fn} ${certificate.dgc.nam.gn}"
		binding.certificateAddName.text = name
		val dateOfBirth = certificate.dgc.dob.parseIsoTimeAndFormat(DEFAULT_DISPLAY_DATE_FORMATTER)
		binding.certificateAddBirthdate.text = dateOfBirth

		val detailItems = CertificateDetailItemListBuilder(recyclerView.context, certificate).buildAll()
		adapter.setItems(detailItems)
	}

}