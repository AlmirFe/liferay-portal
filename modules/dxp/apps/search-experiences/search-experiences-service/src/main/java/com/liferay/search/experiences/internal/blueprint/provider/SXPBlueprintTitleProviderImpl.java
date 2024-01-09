/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.search.experiences.internal.blueprint.provider;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.blueprint.SXPBlueprintTitleProvider;
import com.liferay.search.experiences.model.SXPBlueprint;
import com.liferay.search.experiences.service.SXPBlueprintLocalService;

import javax.portlet.RenderRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Almir Ferreira
 */
@Component(enabled = false, service = SXPBlueprintTitleProvider.class)
public class SXPBlueprintTitleProviderImpl
	implements SXPBlueprintTitleProvider {

	@Override
	public String getSXPBlueprintTitle(
		long companyId, RenderRequest renderRequest,
		String sxpBlueprintExternalReferenceCode) {

		SXPBlueprint sxpBlueprint =
			_sxpBlueprintLocalService.fetchSXPBlueprintByExternalReferenceCode(
				sxpBlueprintExternalReferenceCode, companyId);

		if (sxpBlueprint == null) {
			return StringPool.BLANK;
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		return sxpBlueprint.getTitle(themeDisplay.getLocale());
	}

	@Reference
	private SXPBlueprintLocalService _sxpBlueprintLocalService;

}