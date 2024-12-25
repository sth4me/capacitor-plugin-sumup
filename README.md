# capacitor-sumup-plugin-6

SumUp Capacitor Plugin for capacitor 6, Only for Android Platform

## Install

```bash
npm install capacitor-sumup-plugin-6
npx cap sync
```

## API

<docgen-index>

* [`echo(...)`](#echo)
* [`login(...)`](#login)
* [`checkout(...)`](#checkout)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### echo(...)

```typescript
echo(options: { value: string; }) => Promise<{ value: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ value: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### login(...)

```typescript
login(options: LoginOptions) => Promise<SumUpResponse>
```

| Param         | Type                                                  |
| ------------- | ----------------------------------------------------- |
| **`options`** | <code><a href="#loginoptions">LoginOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#sumupresponse">SumUpResponse</a>&gt;</code>

--------------------


### checkout(...)

```typescript
checkout(options: CheckoutOptions) => Promise<SumUpResponse>
```

| Param         | Type                                                        |
| ------------- | ----------------------------------------------------------- |
| **`options`** | <code><a href="#checkoutoptions">CheckoutOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#sumupresponse">SumUpResponse</a>&gt;</code>

--------------------


### Interfaces


#### SumUpResponse

| Prop          | Type                |
| ------------- | ------------------- |
| **`code`**    | <code>number</code> |
| **`message`** | <code>string</code> |


#### LoginOptions

| Prop               | Type                |
| ------------------ | ------------------- |
| **`affiliateKey`** | <code>string</code> |
| **`accessToken`**  | <code>string</code> |


#### CheckoutOptions

| Prop                       | Type                                    |
| -------------------------- | --------------------------------------- |
| **`total`**                | <code>number</code>                     |
| **`currency`**             | <code>string</code>                     |
| **`title`**                | <code>string</code>                     |
| **`tip`**                  | <code>string</code>                     |
| **`tipOnCardReader`**      | <code>boolean</code>                    |
| **`receiptEmail`**         | <code>string</code>                     |
| **`receiptSMS`**           | <code>string</code>                     |
| **`additionalInfo`**       | <code>{ [key: string]: string; }</code> |
| **`foreignTransactionId`** | <code>string</code>                     |
| **`skipSuccessScreen`**    | <code>boolean</code>                    |
| **`skipFailedScreen`**     | <code>boolean</code>                    |

</docgen-api>
