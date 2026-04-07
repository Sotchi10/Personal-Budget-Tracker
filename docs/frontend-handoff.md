# Frontend Handoff Notes

This document captures how the current Java console app works so the same business flow can be rebuilt as a frontend-only web app.

## Project Shape

- Entry point: `Main.java`
- Domain owner: `User`
- Financial state holder: `Account`
- Business logic services:
  - `TransactionService`
  - `SavingService`
  - `BalanceService`
  - `BudgetLimitService`
  - `WishlistService`
  - `AccountAuth`

## Current User Flow

1. User is created at app startup.
2. User enters `name`, `age`, `email`, `password`, and a 4-digit `passkey`.
3. App shows a menu for income, expense, savings, budget limit, wishlist goals, and transaction history.
4. All financial data lives in memory inside `User -> Account`.

## State Model To Rebuild In Frontend

Recommended frontend state shape:

```ts
type ExpenseCategory =
  | "FOOD"
  | "CLOTHING"
  | "TAX"
  | "TRANSPORTATION"
  | "ENTERTAINMENT"
  | "UTILITIES"
  | "HEALTHCARE"
  | "TECHNOLOGY"
  | "EDUCATION"
  | "OTHER";

type TransactionType = "INCOME" | "EXPENSE" | "ADDSAVING" | "USESAVING";

type RecordItem = {
  id: string;
  type: TransactionType;
  amount: number;
  note: string;
  date: string;
  category?: ExpenseCategory;
};

type WishItem = {
  id: string;
  itemName: string;
  itemPrice: number;
};

type AccountState = {
  balance: number;
  savingAmount: number;
  limitAmount: number;
  records: RecordItem[];
};

type UserState = {
  name: string;
  age: number;
  email: string;
  password: string;
  passkey: string;
  account: AccountState;
  wishLists: WishItem[];
};
```

Persist this with `localStorage`.

## Service Procedures

### 1. Account Creation Procedure

Source: `Main.java`, `User.java`

Rules:
- `name` cannot be blank.
- `age` must be at least 18.
- `email` must match a normal email pattern.
- `password` cannot be blank.
- `passkey` must be exactly 4 digits.

Frontend recommendation:
- Use a first-run onboarding form or modal.
- Save the user profile to `localStorage`.
- Keep the passkey gate for sensitive actions:
  - adding expense
  - using savings

### 2. Income Procedure

Source: `TransactionService.addIncome`

Steps:
1. Read `amount`, `date`, and `note`.
2. Reject if `amount <= 0`.
3. Increase `account.balance`.
4. Create an `INCOME` record.
5. Append record to `account.records`.

UI mapping:
- Add transaction form
- Type selector: `Income`
- Auto-refresh dashboard cards and transaction history

### 3. Expense Procedure

Source: `TransactionService.addExpense`

Steps:
1. Read `amount`, `date`, `category`, `note`, and `passkey`.
2. Reject if `amount <= 0`.
3. Reject if `amount > balance`.
4. Reject if `note` is blank.
5. Reject if `category` is missing.
6. Verify passkey.
7. If a budget limit exists and `totalExpense + amount > limitAmount`, reject.
8. Decrease `account.balance`.
9. Create an `EXPENSE` record with category.
10. Append record to `account.records`.

Important behavior:
- Budget checking is based on total lifetime expense in the current records list.
- It is not monthly yet.

UI mapping:
- Add transaction form
- Category dropdown only when type is `Expense`
- Passkey confirmation modal before submit
- Budget warning banner when user is near or above the limit

### 4. Add Savings Procedure

Source: `SavingService.addSavings`

Steps:
1. Read `amount`, `date`, and `note`.
2. Reject if `amount > balance`.
3. Reject if `amount <= 0`.
4. Move money from `balance` to `savingAmount`.
5. Create an `ADDSAVING` record.
6. Append record to `account.records`.

UI mapping:
- Savings transfer form
- Update balance card and savings card immediately

### 5. Use Savings Procedure

Source: `SavingService.useSavings`

Steps:
1. Read `amount`, `date`, `note`, and `passkey`.
2. Reject if `amount > savingAmount`.
3. Reject if `amount <= 0`.
4. Verify passkey.
5. Move money from `savingAmount` back to `balance`.
6. Create a `USESAVING` record.
7. Append record to `account.records`.

UI mapping:
- Withdraw-from-savings form
- Passkey confirmation modal

### 6. Balance Procedure

Source: `BalanceService`

Behavior:
- `checkBalance` only checks whether balance is exactly zero.
- `showBalance` returns the current balance.

UI mapping:
- Dashboard balance card
- Disable expense/savings transfer actions when balance is zero or insufficient

### 7. Budget Limit Procedure

Source: `BudgetLimitService`

Steps:
1. Read limit amount.
2. Set `account.limitAmount`.

UI mapping:
- Budget settings panel
- Progress bar:
  - numerator: total expense
  - denominator: limit amount

Important note:
- Current Java logic treats the budget as one running cap, not a monthly budget.
- Your concept document mentions monthly budget, so the frontend can intentionally improve this.

### 8. Wishlist / Savings Goal Procedure

Source: `WishlistService`, `WishItems`

Steps:
1. Read item name and target price.
2. Create a wishlist item.
3. Append item to `user.wishLists`.
4. When displayed, calculate progress with:
   - `savingAmount / itemPrice * 100`
   - capped at 100%

UI mapping:
- Savings goals list
- Per-goal progress bar using current total savings

Important behavior:
- Each goal compares against the full shared savings balance.
- Savings are not allocated separately per goal.

### 9. Transaction History Procedure

Source: `TransactionService.showTransaction`

Display includes:
- current savings
- current budget limit
- all records in insertion order
- current balance

Record formatting:
- Income: positive amount
- Expense: negative amount with category
- Add saving: positive savings transfer
- Use saving: negative savings transfer

UI mapping:
- Transaction table or cards
- Filters:
  - by type
  - by category
  - by date
  - by amount

## Frontend Feature Mapping From Concept Doc

From `Smart_Budget_Tracker_Concept.docx`, these features line up well with the Java app:

- Add and manage income and expense records
- Expense categories
- Transaction history
- Current balance
- Budget setting
- Savings goal progress tracker

Natural frontend upgrades:
- real-time dashboard updates
- chart visualizations
- filtering and sorting
- responsive layout
- `localStorage` persistence

## Recommended React Structure

- `App`
- `pages/Dashboard`
- `pages/Transactions`
- `pages/Goals`
- `pages/Settings`
- `components/Sidebar`
- `components/BalanceCard`
- `components/BudgetCard`
- `components/SavingsCard`
- `components/TransactionList`
- `components/TransactionForm`
- `components/SavingsForm`
- `components/BudgetProgress`
- `components/GoalList`
- `components/PasskeyModal`
- `components/charts/ExpensePieChart`
- `components/charts/SpendingBarChart`
- `hooks/useBudgetTracker`
- `utils/storage`
- `utils/validators`

## Recommended Frontend Service Layer

Mirror the Java service responsibilities:

- `authService`
  - verifyPasskey
  - verifyPassword
  - verifyEmail

- `transactionService`
  - addIncome
  - addExpense
  - getTransactionHistory

- `savingService`
  - addSavings
  - useSavings

- `budgetService`
  - setBudgetLimit
  - getBudgetUsage

- `wishlistService`
  - addGoal
  - getGoalProgress

## Gaps And Bugs In Current Java App

These matter if we want to preserve behavior vs improve it:

- `Main.java` case `9` does not set `running = false`, so Exit currently does not exit.
- `User` constructor does not actively validate password strength because the validation call is commented out.
- `User.validatePasskey` prints a message instead of throwing an error.
- `WishItems` setters also print instead of enforcing invalid state with exceptions.
- `Record.record_id` exists but is never assigned or used.
- Budget enforcement is global cumulative expense, not monthly.
- No persistence layer exists in Java; all data resets after app close.

## What To Keep For The Frontend Build

Keep:
- balance, savings, budget limit, wishlist goals, transaction history
- passkey confirmation for sensitive actions
- strict validation on amounts
- expense categorization
- progress tracking for savings goals

Improve:
- make exit/persistence behavior real
- use monthly budget tracking if required by the course concept
- add record IDs
- add edit/delete flows if desired
- add charts and filters
- add responsive UI and better feedback states

## Implementation Direction For The Web App

Best match for your course concept:

1. Build a React app with local component/state management plus `localStorage`.
2. Recreate these service rules in plain TypeScript helper modules or a custom hook.
3. Use a dashboard layout with cards, charts, transaction list, and forms.
4. Add passkey modal checks before expense and savings withdrawal actions.
5. Start with exact Java behavior first, then layer on frontend-only improvements like monthly filtering and charts.
