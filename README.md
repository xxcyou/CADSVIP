# CADSVIP

CAD看图王 (com.stone.app) VIP 判定 LSPosed 模块。

Hook 点（见 /workspace/kduw/逆向分析）：
- `AppSharedPreferences.getUserType()` -> 恒返回 7
- `AppSharedPreferences.isUserVip_*` / `checkUserVIP_Pay` -> 恒 true
- `NewUserInfoModel.getVipType()` -> 恒返回 7

推代码后 GitHub Actions 自动云编译，产物在 Actions 的 artifact 里。
