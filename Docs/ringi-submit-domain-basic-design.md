# 稟議提出機能 基本設計書（Step1: 稟議作成の土台）

## 1. 目的

本書は [doing/ringi-submit-plan.md](../doing/ringi-submit-plan.md) の「1. 稟議作成の土台を作る」に対する基本設計を定義する。

対象は以下に限定する。

- 稟議ドメインモデルの定義
- 必須項目と任意項目の整理（提出時）
- 作成時の初期状態を `DRAFT` にする

## 2. 対象スコープ

### 2.1 対象

- 稟議エンティティ
- 稟議状態の列挙型
- 稟議作成時の初期値ルール
- 申請種別ごとの必須入力ルール

### 2.2 対象外

- 下書き保存時の必須条件
- 提出処理の業務フロー詳細
- 承認/却下機能

下書き保存時の入力許可条件は、次フェーズの実装方針で定義する。

## 3. 業務ルール

### 3.1 申請種別

- `PAID_LEAVE`（有給申請）
- `EXPENSE`（経費申請）

### 3.2 状態

- `DRAFT`（下書き）
- `SUBMITTED`（提出済み）

状態遷移は以下を許可する。

- `DRAFT -> SUBMITTED`

### 3.3 人物関連

- 申請者はログイン中ユーザーを自動設定する。
- 承認者は承認者ロール保有ユーザーのみを候補表示する。
- 承認者は1名を設定する。
- 申請者と承認者が同一ユーザーでも許可する。

### 3.4 申請日

- 画面入力とする。
- デフォルト値は当日を表示する。

### 3.5 金額・用途

- 金額は経費申請時のみ必須とする。
- 金額は1円以上の整数のみ許可する。
- 用途（何に使ったか）は経費申請時のみ必須とする。

## 4. ドメインモデル定義

### 4.1 Ringi（稟議）

| 項目名          | 型       | 必須         | 説明                      |
| --------------- | -------- | ------------ | ------------------------- |
| id              | UUID     | 必須         | 稟議ID                    |
| applicationType | enum     | 必須         | 申請種別（有給/経費）     |
| amount          | integer  | 条件付き必須 | 経費申請時は必須（1以上） |
| purpose         | string   | 条件付き必須 | 経費申請時は必須（用途）  |
| reason          | string   | 必須         | 申請理由                  |
| applicationDate | date     | 必須         | 申請日                    |
| applicantUserId | UUID     | 必須         | 申請者ユーザーID          |
| approverUserId  | UUID     | 必須         | 承認者ユーザーID          |
| status          | enum     | 必須         | 稟議状態                  |
| createdAt       | datetime | 必須         | 作成日時                  |
| updatedAt       | datetime | 必須         | 更新日時                  |

補足:

- `status` は作成時に必ず `DRAFT` を設定する。
- `id` はUUID採番とする。

### 4.2 ApplicationType（申請種別）

- `PAID_LEAVE`
- `EXPENSE`

### 4.3 RingiStatus（稟議状態）

- `DRAFT`
- `SUBMITTED`

## 5. 提出時バリデーション

### 5.1 共通必須（全種別）

- applicationType
- reason
- applicationDate
- applicantUserId
- approverUserId

### 5.2 種別別必須

- `PAID_LEAVE`: 追加必須なし
- `EXPENSE`: amount, purpose が必須

### 5.3 値制約

- `amount >= 1`
- `amount` は整数

## 6. 画面表示ルール

- 有給申請: 申請種別、申請理由、申請日、申請者、承認者
- 経費申請: 申請種別、金額、用途、申請理由、申請日、申請者、承認者

## 7. 初期化ルール

稟議作成時の初期値は以下とする。

- id: UUID自動採番
- status: `DRAFT`
- applicantUserId: ログイン中ユーザーID
- applicationDate: 当日（画面初期表示値）
- createdAt: 現在日時
- updatedAt: 現在日時

## 8. 受け入れ条件（Step1）

- 稟議作成時に `status = DRAFT` で生成される。
- 申請種別が有給の場合、提出時に amount/purpose が不要である。
- 申請種別が経費の場合、提出時に amount/purpose が必須である。
- amount に0以下または小数を入力した場合はバリデーションエラーになる。
- 承認者候補に承認者ロール未保有ユーザーが表示されない。
