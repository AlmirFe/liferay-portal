/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.tuning.rankings.web.internal.upgrade.v2_0_0;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.db.DBInspector;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ClassName;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.search.tuning.rankings.index.Ranking;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Almir Ferreira
 */
public class RenameRankingUpgradeProcess extends UpgradeProcess {

	public RenameRankingUpgradeProcess(
		ClassNameLocalService classNameLocalService) {

		_classNameLocalService = classNameLocalService;
	}

	@Override
	protected void doUpgrade() throws Exception {
		DBInspector dbInspector = new DBInspector(connection);

		DatabaseMetaData metaData = connection.getMetaData();

		ResultSet tablesResultSet = metaData.getTables(
			null, dbInspector.getSchema(), "%", new String[] {"TABLE"});

		while (tablesResultSet.next()) {
			String tableName = tablesResultSet.getString("TABLE_NAME");

			_log.error(tableName);
		}

		try (PreparedStatement preparedStatement = connection.prepareStatement(
				StringBundler.concat(
					"update ", dbInspector.normalizeName("JSONStorageEntry"),
					" set classNameId = ? where classNameId = ?"))) {

			ClassName className = _classNameLocalService.fetchClassName(
				"com.liferay.portal.search.tuning.rankings.web.internal." +
					"index.Ranking");

			if ((className != null) && hasTable("JSONStorageEntry")) {
				_classNameLocalService.deleteClassName(className);

				ClassName newClassName = _classNameLocalService.getClassName(
					Ranking.class.getName());

				preparedStatement.setLong(1, newClassName.getClassNameId());

				preparedStatement.setLong(2, className.getClassNameId());

				preparedStatement.executeUpdate();
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		RenameRankingUpgradeProcess.class);

	private final ClassNameLocalService _classNameLocalService;

}