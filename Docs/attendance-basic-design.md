# 勤怠管理アプリ 基本設計書（ドラフト）

## 1. 目的

本書は、Java/Spring 想定で開発する勤怠管理アプリケーションの基本設計を定義する。今回のスコープは打刻・勤怠修正・勤務表表示（単一組織向けの簡易実装）とする。

## 2. 対象スコープ

- 対象: 打刻（出退勤）、勤怠修正、勤務表（日/週/月）集計、休暇申請（単一承認者）
- 非対象（初期）: 給与連携、ICカード連携、複数会社対応

## 3. 業務ルール（要約）

- 対象は単一組織（小規模）
- 打刻は Web ブラウザ（PC/モバイル）で行う
- 承認ワークフローは単一承認者（申請者→承認者1名）
- 休暇種別: 有給休暇、欠勤/病欠、特別休暇、育児/介護、振替休日
- 法令関連: 時間外（残業）集計、深夜割増（22:00-5:00）対応

## 4. ドメインモデル（主要エンティティ）

4.1 TimeEntry（打刻）

- id: UUID
- userId: UUID
- date: date（打刻対象日）
- clockIn: datetime（出勤時刻）
- clockOut: datetime（退勤時刻）
- workMinutes: integer（所定労働分数、計算値）
- note: string（任意）
- createdAt: datetime
- updatedAt: datetime

4.2 LeaveRequest（休暇申請）

- id: UUID
- userId: UUID
- leaveType: enum（LeaveType）
- startDate: date
- endDate: date
- reason: string
- approverUserId: UUID
- status: enum（DRAFT/SUBMITTED/APPROVED/REJECTED）
- createdAt: datetime
- updatedAt: datetime

4.3 列挙型

- LeaveType: PAID_LEAVE, SICK_LEAVE, SPECIAL_LEAVE, CHILDCARE, SUBSTITUTE
- UserRole: USER, APPROVER, ADMIN

## 5. 状態遷移

- LeaveRequest: DRAFT -> SUBMITTED -> (APPROVED | REJECTED)
- TimeEntry: 基本は編集可能（保存・確定の状態管理は実装フェーズで定義）

## 6. バリデーション

- 打刻: clockIn が clockOut より前であること（退勤が未入力の場合は許容）
- 勤務時間: マイナス値は不可、分単位での丸めは設定により可
- 休暇申請: startDate <= endDate、必須項目のチェック（leaveType, reason, approverUserId）
- 残業/深夜: 時間帯に応じた集計ルールを適用（22:00-5:00 を深夜とする）

## 7. 画面表示/項目（主要画面）

- 打刻画面: 出勤ボタン、退勤ボタン、当日の勤務時間表示、手動編集リンク
- 勤務表画面: 日/週/月切替、各日の出退勤時刻、労働時間、残業時間、深夜時間
- 休暇申請画面: 種別、開始日、終了日、理由、承認者選択、申請ボタン
- 管理者画面: ユーザー管理、承認処理（承認/却下）、CSVエクスポート

## 8. 初期化ルール

- TimeEntry 作成時のデフォルト: createdAt/updatedAt を現在時刻で設定
- LeaveRequest 作成時のデフォルト: status = DRAFT、applicantUserId はログインユーザー

## 9. 受け入れ条件（初期）

- 打刻（出退勤）がブラウザで実行でき、日次の勤務表に反映される
- 勤怠修正（手動入力）で打刻時刻を編集可能である
- 休暇申請を作成し、単一承認者に提出→承認/却下ができる
- 残業時間と深夜時間が勤務表で集計される

## 10. 非機能（簡易）

- 想定ユーザー: 小規模（数十〜数百ユーザー）
- 認証: ローカル認証（メール/パスワード）とロール管理
- データ保持: 現状は指定なし（後で保存期間を追加）

---
