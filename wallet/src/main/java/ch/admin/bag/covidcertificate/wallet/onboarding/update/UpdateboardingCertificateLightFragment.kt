/*
 * Copyright (c) 2021 Ubique Innovation AG <https://www.ubique.ch>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * SPDX-License-Identifier: MPL-2.0
 */

package ch.admin.bag.covidcertificate.wallet.onboarding.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ch.admin.bag.covidcertificate.common.util.UrlUtil
import ch.admin.bag.covidcertificate.wallet.R
import ch.admin.bag.covidcertificate.wallet.databinding.FragmentUpdateboardingCertificateLightBinding
import ch.admin.bag.covidcertificate.wallet.onboarding.OnboardingActivity

class UpdateboardingCertificateLightFragment : Fragment(R.layout.fragment_updateboarding_certificate_light) {

	companion object {
		fun newInstance() = UpdateboardingCertificateLightFragment()
	}

	private var _binding: FragmentUpdateboardingCertificateLightBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		_binding = FragmentUpdateboardingCertificateLightBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		binding.onboardingContinueButton.setOnClickListener {
			(requireActivity() as OnboardingActivity).continueToNextPage()
		}

		binding.itemAgbLink.setOnClickListener { v ->
			val url = getString(R.string.wallet_terms_privacy_link)
			UrlUtil.openUrl(v.context, url)
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

}
