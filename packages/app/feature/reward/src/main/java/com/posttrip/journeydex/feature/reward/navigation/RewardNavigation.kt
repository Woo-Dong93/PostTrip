package com.posttrip.journeydex.feature.reward.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val rewardNavigationRoute = "reward_route"
const val termsNavigationRoute = "terms_route"
const val privacyNavigationRoute = "privacy_route"

fun NavController.navigateToReward(navOptions: NavOptions? = null) {
    this.navigate(rewardNavigationRoute, navOptions)
}


fun NavController.navigateToTerms(navOptions: NavOptions? = null) {
    this.navigate(termsNavigationRoute, navOptions)
}

fun NavController.navigateToPrivacy(navOptions: NavOptions? = null) {
    this.navigate(privacyNavigationRoute, navOptions)
}

fun NavGraphBuilder.rewardScreen(
    onBackClick: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onWithdrawClick: () -> Unit
) {
    composable(
        route = rewardNavigationRoute
    ) {
        SettingScreen(
            onBackClick = onBackClick,
            onTermsClick = onTermsClick,
            onPrivacyClick = onPrivacyClick,
            onLogoutClick = onLogoutClick,
            onWithdrawClick = onWithdrawClick
        )
    }
}

fun NavGraphBuilder.termsScreen(
    onBackClick: () -> Unit
) {
    composable(
        termsNavigationRoute
    ) {
        TextScreen(onBackClick = onBackClick, title = "서비스이용약관", text = "")
    }
}

fun NavGraphBuilder.privacyScreen(
    onBackClick: () -> Unit
) {
    composable(
        privacyNavigationRoute
    ) {
        TextScreen(onBackClick = onBackClick, title = "개인정보처리방침", text = "" +
                "앱의 개인정보처리방침은 사용자로부터 수집한 개인정보를 어떻게 처리하는지 명확하게 설명하는 중요한 문서입니다. 이 문서는 법적 요구 사항을 충족하고, 사용자가 자신의 정보가 어떻게 사용되는지 이해할 수 있도록 해야 합니다. 아래는 앱의 개인정보처리방침에 포함되어야 할 주요 항목과 각 항목의 예시 내용을 제시합니다.\n" +
                "\n" +
                "### 개인정보처리방침 예시 내용\n" +
                "\n" +
                "1. **개인정보 수집 항목 및 방법**\n" +
                "   - **예시**:\n" +
                "     - 당사는 사용자가 앱을 사용할 때 다음과 같은 개인정보를 수집할 수 있습니다:\n" +
                "       - 이름, 이메일 주소, 전화번호 등 사용자 식별 정보\n" +
                "       - 위치 정보, IP 주소, 기기 정보 등 자동으로 수집되는 정보\n" +
                "       - 앱 사용 기록, 오류 로그, 사용자 피드백 등 서비스 이용 정보\n" +
                "   - **수집 방법**:\n" +
                "     - 사용자가 직접 입력한 정보 (예: 회원가입, 문의 시)\n" +
                "     - 자동으로 수집되는 정보 (예: 앱 사용 시 자동 로그 기록)\n" +
                "\n" +
                "2. **개인정보의 수집 및 이용 목적**\n" +
                "   - **예시**:\n" +
                "     - 회원 관리: 사용자 식별 및 계정 관리\n" +
                "     - 서비스 제공: 맞춤형 서비스 제공 및 이용자 요청 처리\n" +
                "     - 마케팅 및 광고: 이벤트 정보 제공 및 마케팅 자료 전달\n" +
                "     - 서비스 개선: 앱 사용 경험 향상 및 서비스 개발\n" +
                "\n" +
                "3. **개인정보의 보유 및 이용 기간**\n" +
                "   - **예시**:\n" +
                "     - 당사는 개인정보 수집 및 이용 목적이 달성된 후에는 해당 정보를 지체 없이 파기합니다. 단, 법령에 따라 보관이 필요한 경우 해당 기간 동안 보유합니다.\n" +
                "     - 예: 회원 탈퇴 시까지 보관, 법령에 따라 거래 기록은 5년간 보관.\n" +
                "\n" +
                "4. **개인정보의 제3자 제공**\n" +
                "   - **예시**:\n" +
                "     - 당사는 사용자의 동의 없이 개인정보를 외부에 제공하지 않습니다. 단, 법령에 따른 요청이 있을 경우, 또는 서비스 제공에 필요한 경우(예: 결제 처리 시) 사전에 사용자의 동의를 받아 제공합니다.\n" +
                "\n" +
                "5. **개인정보 처리 위탁**\n" +
                "   - **예시**:\n" +
                "     - 당사는 원활한 서비스 제공을 위하여 개인정보 처리를 외부 전문업체에 위탁할 수 있으며, 위탁 시 사용자의 개인정보가 안전하게 처리될 수 있도록 관리·감독합니다.\n" +
                "\n" +
                "6. **사용자의 권리 및 행사 방법**\n" +
                "   - **예시**:\n" +
                "     - 사용자는 언제든지 자신의 개인정보를 조회, 수정, 삭제할 수 있습니다.\n" +
                "     - 개인정보와 관련한 권리 행사는 앱 내 설정 메뉴 또는 고객센터를 통해 요청할 수 있습니다.\n" +
                "\n" +
                "7. **개인정보의 파기 절차 및 방법**\n" +
                "   - **예시**:\n" +
                "     - 당사는 개인정보 보유 기간이 경과하거나 처리 목적이 달성된 후에는 해당 정보를 지체 없이 파기합니다.\n" +
                "     - 전자적 파일 형태의 정보는 복구가 불가능한 방법으로 삭제하고, 종이 문서 형태의 정보는 분쇄하거나 소각하여 파기합니다.\n" +
                "\n" +
                "8. **개인정보 보호를 위한 기술적·관리적 대책**\n" +
                "   - **예시**:\n" +
                "     - 개인정보는 암호화하여 저장 및 관리되며, 내부적으로 접근이 제한됩니다.\n" +
                "     - 정기적인 보안 점검 및 직원 교육을 통해 개인정보 보호를 강화하고 있습니다.\n" +
                "\n" +
                "9. **개인정보 보호책임자 및 연락처**\n" +
                "   - **예시**:\n" +
                "     - 개인정보 보호책임자: [담당자 이름]\n" +
                "     - 연락처: [이메일 주소, 전화번호]\n" +
                "     - 사용자는 개인정보와 관련된 문의, 불만을 개인정보 보호책임자에게 문의할 수 있습니다.\n" +
                "\n" +
                "10. **개인정보처리방침의 변경**\n" +
                "    - **예시**:\n" +
                "      - 본 개인정보처리방침은 시행일로부터 적용되며, 법령 및 내부 방침에 따라 변경될 수 있습니다. 변경사항은 앱 내 공지사항을 통해 고지됩니다.\n" +
                "\n" +
                "### 추가 고려 사항\n" +
                "- **사용자의 동의**: 개인정보 수집 시 사용자의 동의를 명확히 받고, 수집 목적에 대해 충분히 설명해야 합니다.\n" +
                "- **정기적인 업데이트**: 개인정보처리방침은 법령 변경이나 서비스의 변경에 따라 주기적으로 업데이트되어야 합니다.\n" +
                "- **접근성**: 개인정보처리방침은 사용자가 쉽게 접근할 수 있도록 앱 내 설정 메뉴나 메인 페이지에 링크를 제공하는 것이 좋습니다.\n" +
                "\n" +
                "이 내용들은 예시일 뿐이며, 실제로는 앱의 기능, 수집하는 정보의 종류, 법적 요구사항 등을 고려하여 작성해야 합니다. 법적 자문을 받아 정확하고 신뢰할 수 있는 개인정보처리방침을 마련하는 것이 중요합니다.")
    }
}
