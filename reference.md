# Reference
## AccessTokens
<details><summary><code>client.accessTokens.create(request) -> AccessToken</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Create a short-lived access token for authenticating API requests. When using API key authentication, provide company_id or user_id. When using OAuth, the user is derived from the token. Use this token with Whop's web and mobile embedded components.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.accessTokens().create(
    CreateAccessTokensRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**companyId:** `Optional<String>` — The unique identifier of the company to generate the token for, starting with 'biz_'. The API key must have permission to access this company.
    
</dd>
</dl>

<dl>
<dd>

**expiresAt:** `Optional<OffsetDateTime>` — The expiration timestamp for the access token. Defaults to 1 hour from now, with a maximum of 3 hours.
    
</dd>
</dl>

<dl>
<dd>

**scopedActions:** `Optional<List<String>>` — An array of permission scopes to grant to the access token. If empty or omitted, all permissions from the authenticating credential are inherited. Must be a subset of the credential's permissions.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — The unique identifier of the user to generate the token for, starting with 'user_'. The API key must have permission to access this user.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## AccountLinks
<details><summary><code>client.accountLinks.create(request) -> AccountLink</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Generate a URL that directs a sub-merchant to their account portal, such as the hosted payouts dashboard or the KYC onboarding flow.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.accountLinks().create(
    CreateAccountLinksRequest
        .builder()
        .companyId("biz_xxxxxxxxxxxxxx")
        .refreshUrl("refresh_url")
        .returnUrl("return_url")
        .useCase(AccountLinkUseCases.ACCOUNT_ONBOARDING)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**companyId:** `String` — The unique identifier of the company to generate the link for, starting with 'biz_'. Must be a sub-merchant of the API key's company.
    
</dd>
</dl>

<dl>
<dd>

**refreshUrl:** `String` — The URL to redirect the user to if the session expires and needs to be re-authenticated, such as 'https://example.com/refresh'.
    
</dd>
</dl>

<dl>
<dd>

**returnUrl:** `String` — The URL to redirect the user to when they want to return to your site, such as 'https://example.com/return'.
    
</dd>
</dl>

<dl>
<dd>

**useCase:** `AccountLinkUseCases` — The purpose of the account link, such as hosted payouts portal or hosted KYC onboarding.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Accounts
<details><summary><code>client.accounts.list() -> SyncPagingIterable&amp;lt;Account&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists accounts visible to the credential. User tokens return the user's business accounts; Account API keys return the requesting account and its connected accounts. Pass `parent_account_id` to return only that parent account's connected accounts.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.accounts().list(
    ListAccountsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of accounts to return (default 10, max 50).
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor; returns accounts after this position.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — The number of accounts to return from the end of the range.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — A cursor; returns accounts before this position.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListAccountsRequestOrder>` — The field to sort accounts by. `volume` requires `stats:read` on the parent account.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListAccountsRequestDirection>` — Sort direction.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<ListAccountsRequestStatus>` — Return only accounts with this status: `active` (includes accounts that have not entered payments review) or `suspended`.
    
</dd>
</dl>

<dl>
<dd>

**query:** `Optional<String>` — Free-text filter on account title or ID. `%` and `_` match literally.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<OffsetDateTime>` — Return only accounts created after this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<OffsetDateTime>` — Return only accounts created before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**volumeMin:** `Optional<Double>` — Return only accounts whose lifetime USD volume is at least this value. Requires `stats:read` on the parent account.
    
</dd>
</dl>

<dl>
<dd>

**volumeMax:** `Optional<Double>` — Return only accounts whose lifetime USD volume is at most this value. Requires `stats:read` on the parent account.
    
</dd>
</dl>

<dl>
<dd>

**parentAccountId:** `Optional<String>` — For platforms: the parent account ID whose direct connected accounts to return. Requires `payout:account:read` on the parent account.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.accounts.create(request) -> Account</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Creates an account. User tokens create business accounts; Account API keys create connected accounts. Tax fields (`tax_remitted_by`, `tax_type`, `product_tax_code_id`, `business_address`, `tax_identifiers`) are configured with Update Account, not at creation.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.accounts().create(
    CreateAccountsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**affiliateCode:** `Optional<String>` — The username, if any, of the partner who referred this account
    
</dd>
</dl>

<dl>
<dd>

**country:** `Optional<String>` — The ISO 3166-1 alpha-2 country code where the account's business is located (e.g. `US`). Defaults to the parent account's country for connected accounts.
    
</dd>
</dl>

<dl>
<dd>

**email:** `Optional<String>` — The email address of the account owner. Required for Account API key requests.
    
</dd>
</dl>

<dl>
<dd>

**metadata:** `Optional<Map<String, Object>>` — Arbitrary key/value metadata to store on the account.
    
</dd>
</dl>

<dl>
<dd>

**title:** `Optional<String>` — The display name of the account. Defaults to `metadata.external_id` or the owner's email when omitted.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.accounts.me() -> Account</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the account associated with the current Account API key.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.accounts().me();
```
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.accounts.retrieve(id) -> Account</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a single account by ID or public route when it is visible to the credential, including its crypto wallet. The reserved id `me` retrieves the account associated with the current Account API key; user tokens have no single account, so they must address one by ID or route.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.accounts().retrieve(
    "id",
    RetrieveAccountsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Account ID, prefixed `biz_`, its public route, or `me` for the account associated with the current API key.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.accounts.update(id, request) -> Account</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Updates an account. User tokens can update business accounts; Account API keys can update connected accounts. The reserved id `me` — accepted on Retrieve Account — resolves to the requesting account, which an Account API key cannot edit, so updates must name the connected account by its `biz_` id.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.accounts().update(
    "id",
    UpdateAccountsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Account ID, prefixed `biz_`.
    
</dd>
</dl>

<dl>
<dd>

**affiliateApplicationRequired:** `Optional<Boolean>` — Whether prospective affiliates must submit an application before promoting this account.
    
</dd>
</dl>

<dl>
<dd>

**affiliateInstructions:** `Optional<String>` — Guidelines shown to affiliates promoting this account.
    
</dd>
</dl>

<dl>
<dd>

**bannerImage:** `Optional<UpdateAccountsRequestBannerImage>` — Account banner image, used as the cover photo when creating a Whop-managed Facebook page. Image files up to 10 MB, except `image/gif`. Pass a JSON object containing an `id` from [Create File](/api-reference/files/create-file).
    
</dd>
</dl>

<dl>
<dd>

**businessAddress:** `Optional<UpdateAccountsRequestBusinessAddress>` — Account business address used to calculate tax. A complete address in a supported country is required when `tax_remitted_by` is `self`.
    
</dd>
</dl>

<dl>
<dd>

**businessName:** `Optional<String>` — The legal business name used with the account's tax address.
    
</dd>
</dl>

<dl>
<dd>

**businessType:** `Optional<UpdateAccountsRequestBusinessType>` — High-level business category for the account. See the [business types and industries glossary](/api-reference/beta/accounts/account#business-types-and-industries-glossary) for valid values.
    
</dd>
</dl>

<dl>
<dd>

**collectVatId:** `Optional<Boolean>` — Whether checkout shows a VAT/tax ID field for buyers to optionally enter. Does not require a VAT ID to purchase.
    
</dd>
</dl>

<dl>
<dd>

**country:** `Optional<String>` — Country where the account is located.
    
</dd>
</dl>

<dl>
<dd>

**description:** `Optional<String>` — Account promotional description. When creating a Whop-managed Facebook page, it is truncated to 155 characters and used as the About text.
    
</dd>
</dl>

<dl>
<dd>

**featuredAffiliateProductId:** `Optional<String>` — The ID of the product to feature for affiliates. Pass `null` to clear.
    
</dd>
</dl>

<dl>
<dd>

**homePreferences:** `Optional<List<UpdateAccountsRequestHomePreferencesItem>>` — Public account home page preferences.
    
</dd>
</dl>

<dl>
<dd>

**industryGroup:** `Optional<UpdateAccountsRequestIndustryGroup>` — Account industry group. See the [business types and industries glossary](/api-reference/beta/accounts/account#business-types-and-industries-glossary) for valid values.
    
</dd>
</dl>

<dl>
<dd>

**industryType:** `Optional<String>` — Specific industry vertical for the account. See the [business types and industries glossary](/api-reference/beta/accounts/account#business-types-and-industries-glossary) for valid values.
    
</dd>
</dl>

<dl>
<dd>

**invoicePrefix:** `Optional<String>` — Prefix used for account invoices.
    
</dd>
</dl>

<dl>
<dd>

**logo:** `Optional<UpdateAccountsRequestLogo>` — Account logo, used as the profile picture when creating a Whop-managed Facebook page. Image files up to 5 MB. Pass a JSON object containing an `id` from [Create File](/api-reference/files/create-file).
    
</dd>
</dl>

<dl>
<dd>

**metadata:** `Optional<Map<String, Object>>` — Arbitrary key/value metadata to store on the account.
    
</dd>
</dl>

<dl>
<dd>

**onboardingType:** `Optional<UpdateAccountsRequestOnboardingType>` — The type of onboarding the account has completed.
    
</dd>
</dl>

<dl>
<dd>

**opengraphImage:** `Optional<UpdateAccountsRequestOpengraphImage>` — Open Graph preview media used when the account is shared. Image and video files up to 5 MB. Pass a JSON object containing an `id` from [Create File](/api-reference/files/create-file).
    
</dd>
</dl>

<dl>
<dd>

**opengraphImageVariant:** `Optional<UpdateAccountsRequestOpengraphImageVariant>` — The account Open Graph image variant.
    
</dd>
</dl>

<dl>
<dd>

**otherBusinessDescription:** `Optional<String>` — The description of the business type when business_type is other.
    
</dd>
</dl>

<dl>
<dd>

**otherIndustryDescription:** `Optional<String>` — The description of the industry type when industry_type is other.
    
</dd>
</dl>

<dl>
<dd>

**productTaxCodeId:** `Optional<String>` — ID of the tax classification code applied by default to the account's products. See the available [product categories](https://docs.numeral.com/essentials/product-categories).
    
</dd>
</dl>

<dl>
<dd>

**require2Fa:** `Optional<Boolean>` — Whether the account requires authorized users to have two-factor authentication enabled.
    
</dd>
</dl>

<dl>
<dd>

**route:** `Optional<String>` — The unique URL slug for the account.
    
</dd>
</dl>

<dl>
<dd>

**sendCustomerEmails:** `Optional<Boolean>` — Whether Whop sends transactional emails to customers on behalf of this account.
    
</dd>
</dl>

<dl>
<dd>

**showJoinedWhops:** `Optional<Boolean>` — Whether the account appears in joined whops on other accounts.
    
</dd>
</dl>

<dl>
<dd>

**showReviewsDtc:** `Optional<Boolean>` — Whether reviews are displayed on direct-to-consumer product pages.
    
</dd>
</dl>

<dl>
<dd>

**showUserDirectory:** `Optional<Boolean>` — Whether the account shows users in the user directory.
    
</dd>
</dl>

<dl>
<dd>

**socialLinks:** `Optional<List<Map<String, Object>>>` — The full list of social links to display for the account.
    
</dd>
</dl>

<dl>
<dd>

**storePageConfig:** `Optional<UpdateAccountsRequestStorePageConfig>` — Account store page display configuration.
    
</dd>
</dl>

<dl>
<dd>

**targetAudience:** `Optional<String>` — The target audience for this account.
    
</dd>
</dl>

<dl>
<dd>

**taxCollectionEnabledStates:** `Optional<List<UpdateAccountsRequestTaxCollectionEnabledStatesItem>>` — US state codes (50 states plus `DC`) where the account collects tax. Replaces the full set on update. Only settable when `tax_remitted_by` is `self`.
    
</dd>
</dl>

<dl>
<dd>

**taxIdentifiers:** `Optional<List<UpdateAccountsRequestTaxIdentifiersItem>>` — Account tax/VAT registrations to add or update. When `tax_remitted_by` is `self`, tax is calculated and collected only in the countries where the account holds a registration.
    
</dd>
</dl>

<dl>
<dd>

**taxRemittedBy:** `Optional<UpdateAccountsRequestTaxRemittedBy>` — Determines whether Whop or the account calculates and remits tax. The account must provide a supported-country business address when it self-remits.
    
</dd>
</dl>

<dl>
<dd>

**taxType:** `Optional<UpdateAccountsRequestTaxType>` — Determines whether tax is included in the listed price or added at checkout.
    
</dd>
</dl>

<dl>
<dd>

**threeDsLevel:** `Optional<UpdateAccountsRequestThreeDsLevel>` — Account-level 3D Secure behavior. Set `mandate_challenge` to require cardholder verification on supported card payments, or `null` to use the standard checkout flow.
    
</dd>
</dl>

<dl>
<dd>

**title:** `Optional<String>` — The display name of the account.
    
</dd>
</dl>

<dl>
<dd>

**useLogoAsOpengraphImageFallback:** `Optional<Boolean>` — Whether the account uses its logo as the fallback Open Graph image.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.accounts.formCompany(id, request) -> FormCompanyAccountsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Starts an LLC or C-Corp formation for a business account. Defaults to an LLC; set `entity_type` to `c_corp` to form a C-Corp, which additionally requires `share_structure` and officer `roles` on every founder. On submission, the application is validated and the response returns a hosted checkout URL. Once paid, the filing is submitted. Track progress through the account's [`company_formation`](/api-reference/beta/accounts/retrieve-account) field on Retrieve Account.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.accounts().formCompany(
    "id",
    FormCompanyAccountsRequest
        .builder()
        .businessName("Shine Time Auto Detailing")
        .businessType("brick_and_mortar")
        .formationState(FormCompanyAccountsRequestFormationState.TX)
        .industryGroup("automotive")
        .industryType("car_wash")
        .founders(
            Arrays.asList(
                FormCompanyAccountsRequestFoundersItem
                    .builder()
                    .address(
                        FormCompanyAccountsRequestFoundersItemAddress
                            .builder()
                            .city("Austin")
                            .country("US")
                            .line1("907 Ridgemont Dr")
                            .postalCode("78704")
                            .state("TX")
                            .line2("Apt 4")
                            .build()
                    )
                    .email("marcus@shinetime.example")
                    .firstName("Marcus")
                    .isPrimary(true)
                    .lastName("Webb")
                    .phone("+15125550142")
                    .dateOfBirth("1988-03-14")
                    .ownershipPercentage(100.0)
                    .roles(
                        Optional.of(
                            Arrays.asList(FormCompanyAccountsRequestFoundersItemRolesItem.PRESIDENT)
                        )
                    )
                    .ssn("123-45-6789")
                    .build()
            )
        )
        .businessAddress(
            FormCompanyAccountsRequestBusinessAddress
                .builder()
                .city("Austin")
                .country("US")
                .line1("4180 Burnet Rd")
                .postalCode("78756")
                .state("TX")
                .line2("Suite 2")
                .build()
        )
        .businessPhone("+15125550142")
        .businessWebsite("https://shinetime.example")
        .entitySuffix(FormCompanyAccountsRequestEntitySuffix.LLC)
        .entityType(FormCompanyAccountsRequestEntityType.LLC)
        .expediteEin(true)
        .shareStructure(
            FormCompanyAccountsRequestShareStructure
                .builder()
                .numberOfShares(123)
                .value(123.0)
                .build()
        )
        .useRegisteredAgent(true)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Account ID, prefixed `biz_`.
    
</dd>
</dl>

<dl>
<dd>

**businessAddress:** `Optional<FormCompanyAccountsRequestBusinessAddress>` — Company mailing address. Required unless `use_registered_agent` is `true`.
    
</dd>
</dl>

<dl>
<dd>

**businessName:** `String` — Legal name for the new company.
    
</dd>
</dl>

<dl>
<dd>

**businessPhone:** `Optional<String>` — Business phone number in E.164 format, for example `+12125550100`. Required unless `use_registered_agent` is `true`.
    
</dd>
</dl>

<dl>
<dd>

**businessType:** `String` — High-level business category, from the Whop business taxonomy. Valid values are listed on [business types and industries glossary](/api-reference/beta/accounts/account#business-types-and-industries-glossary).
    
</dd>
</dl>

<dl>
<dd>

**businessWebsite:** `Optional<String>` — Company website URL.
    
</dd>
</dl>

<dl>
<dd>

**entitySuffix:** `Optional<FormCompanyAccountsRequestEntitySuffix>` — Legal entity ending appended to `business_name`. LLC formations accept `LLC`, `L.L.C`, `L.L.C.` or `Limited Liability Company` and default to `LLC`; C-Corp formations accept `Inc`, `Inc.`, `Incorporated`, `Corp.`, `Corporation`, `C Corp`, `C Corporation`, `CCorp` or `Company` and default to `Inc.`. Unrecognized values fall back to the default for the entity type.
    
</dd>
</dl>

<dl>
<dd>

**entityType:** `Optional<FormCompanyAccountsRequestEntityType>` — Legal entity type to form. Defaults to `llc`.
    
</dd>
</dl>

<dl>
<dd>

**expediteEin:** `Optional<Boolean>` — Request expedited EIN processing for an additional fee. Available only when no founder supplies an SSN.
    
</dd>
</dl>

<dl>
<dd>

**formationState:** `FormCompanyAccountsRequestFormationState` — Two-letter code of the US state (or `DC`) to form the company in.
    
</dd>
</dl>

<dl>
<dd>

**founders:** `List<FormCompanyAccountsRequestFoundersItem>` — The company's founders. Exactly one must be marked `is_primary` — the responsible party for the filing.
    
</dd>
</dl>

<dl>
<dd>

**industryGroup:** `String` — Industry group, from the Whop business taxonomy. Valid values are listed on [business types and industries glossary](/api-reference/beta/accounts/account#business-types-and-industries-glossary).
    
</dd>
</dl>

<dl>
<dd>

**industryType:** `String` — Specific industry vertical, from the Whop business taxonomy. Valid values are listed on [business types and industries glossary](/api-reference/beta/accounts/account#business-types-and-industries-glossary).
    
</dd>
</dl>

<dl>
<dd>

**shareStructure:** `Optional<FormCompanyAccountsRequestShareStructure>` — Authorized share structure. Required when `entity_type` is `c_corp`; ignored for LLCs.
    
</dd>
</dl>

<dl>
<dd>

**useRegisteredAgent:** `Optional<Boolean>` — Use the registered agent's address as the company address instead of `business_address`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.accounts.transferOwnership(id, request) -> TransferOwnershipAccountsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Transfers ownership of the account to another user, identified by user ID or email address. If the recipient already holds the owner role, ownership moves immediately; otherwise they get an invite and ownership moves when they accept.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.accounts().transferOwnership(
    "id",
    TransferOwnershipAccountsRequest
        .builder()
        .identifier("marcus@shinetime.example")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Account ID, prefixed `biz_`.
    
</dd>
</dl>

<dl>
<dd>

**asPartner:** `Optional<Boolean>` — If true, the current owner is credited as the account's Whop partner, earning partner commission on its sales. Requires the current owner to already be an enrolled Whop partner. Skipped if the account already has an active partner.
    
</dd>
</dl>

<dl>
<dd>

**identifier:** `String` — The user to transfer ownership to: a user ID (`user_*`) or an email address. An email address with no Whop account yet is sent an invite to create one.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Ad Campaigns
<details><summary><code>client.adCampaigns.list() -> SyncPagingIterable&amp;lt;AdCampaign&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the ad campaigns for an account, with stats over the requested window.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.adCampaigns().list(
    ListAdCampaignsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The account the campaigns belong to. Defaults to the account-scoped key's own account.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<ListAdCampaignsRequestStatus>` — Only return campaigns with this status.
    
</dd>
</dl>

<dl>
<dd>

**query:** `Optional<String>` — Filter campaigns by a title or ID substring.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListAdCampaignsRequestOrder>` — The field to sort by. Defaults to created_at. Stat columns (spend, impressions, …) rank over the stats_from/stats_to window across the whole list, not just the current page. results, cost_per_result and return_on_ad_spend rank by the same Whop pixel-attributed values the response reports.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListAdCampaignsRequestDirection>` — The sort direction. Defaults to desc.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Only return campaigns created before this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Only return campaigns created after this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**statsFrom:** `Optional<String>` — Start of the stats window. Defaults to all-time.
    
</dd>
</dl>

<dl>
<dd>

**statsTo:** `Optional<String>` — End of the stats window. Defaults to now.
    
</dd>
</dl>

<dl>
<dd>

**timeZone:** `Optional<String>` — IANA timezone (e.g. America/New_York) the stats window is interpreted in. Bare stats_from/stats_to dates resolve to day boundaries on this clock. Defaults to UTC.
    
</dd>
</dl>

<dl>
<dd>

**attributionModel:** `Optional<ListAdCampaignsRequestAttributionModel>` — Attribution model the conversion stats count under (defaults to last_touch). Under both models a journey with any whop ad touch attributes to whop; the model picks which whop touch credits the entity and which non-whop source wins otherwise.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of campaigns to return.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor to fetch the page after (from page_info.end_cursor).
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — The number of campaigns to return from the end of the range.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Cursor to fetch the page before (from page_info.start_cursor).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.adCampaigns.create(request) -> AdCampaign</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Creates an ad campaign for an account.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.adCampaigns().create(
    CreateAdCampaignsRequest
        .builder()
        .objective(CreateAdCampaignsRequestObjective.AWARENESS)
        .platform(CreateAdCampaignsRequestPlatform.META)
        .title("Now hiring mobile detailers — Austin")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The account to create the campaign under. Defaults to the account-scoped key's own account.
    
</dd>
</dl>

<dl>
<dd>

**bidType:** `Optional<CreateAdCampaignsRequestBidType>` — How delivery bids in the ad auction: `minimum_cost` gets the most results for the budget, `average_target` holds an average cost per result, `maximum_target` never bids above a cap. Only for campaigns that own the budget.
    
</dd>
</dl>

<dl>
<dd>

**budgetAmount:** `Optional<Double>` — The campaign's budget, in the ad account's currency. Required when budget_optimization is `ad_campaign`; omit when each ad group sets its own budget.
    
</dd>
</dl>

<dl>
<dd>

**budgetOptimization:** `Optional<CreateAdCampaignsRequestBudgetOptimization>` — Which level owns the budget: the whole campaign (`ad_campaign`) or each ad group individually (`ad_group`). Defaults to `ad_group`.
    
</dd>
</dl>

<dl>
<dd>

**budgetType:** `Optional<CreateAdCampaignsRequestBudgetType>` — Whether the budget is spent per day (`daily`) or over the campaign's full run (`lifetime`). Defaults to `daily`.
    
</dd>
</dl>

<dl>
<dd>

**desiredCostPerResult:** `Optional<Double>` — Cost per result to aim for (`average_target`) or never exceed (`maximum_target`). Only for campaigns that own the budget.
    
</dd>
</dl>

<dl>
<dd>

**endsAt:** `Optional<String>` — When the campaign stops delivering, as an ISO 8601 timestamp. Only for campaigns that own the budget.
    
</dd>
</dl>

<dl>
<dd>

**objective:** `CreateAdCampaignsRequestObjective` — The goal the campaign optimizes toward.
    
</dd>
</dl>

<dl>
<dd>

**platform:** `CreateAdCampaignsRequestPlatform` — The ad network the campaign runs on.
    
</dd>
</dl>

<dl>
<dd>

**specialAdCategories:** `Optional<List<CreateAdCampaignsRequestSpecialAdCategoriesItem>>` — Regulated categories the campaign falls under. Ads in these categories are subject to extra targeting restrictions.
    
</dd>
</dl>

<dl>
<dd>

**startsAt:** `Optional<String>` — When the campaign starts delivering, as an ISO 8601 timestamp. Only for campaigns that own the budget.
    
</dd>
</dl>

<dl>
<dd>

**title:** `String` — The title of the campaign.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.adCampaigns.retrieve(id) -> AdCampaign</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a single ad campaign with stats over the requested window.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.adCampaigns().retrieve(
    "id",
    RetrieveAdCampaignsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ad campaign ID.
    
</dd>
</dl>

<dl>
<dd>

**statsFrom:** `Optional<String>` — Start of the stats window.
    
</dd>
</dl>

<dl>
<dd>

**statsTo:** `Optional<String>` — End of the stats window.
    
</dd>
</dl>

<dl>
<dd>

**timeZone:** `Optional<String>` — IANA timezone the stats window is interpreted in. Defaults to UTC.
    
</dd>
</dl>

<dl>
<dd>

**attributionModel:** `Optional<RetrieveAdCampaignsRequestAttributionModel>` — Attribution model the conversion stats count under (defaults to last_touch). Under both models a journey with any whop ad touch attributes to whop; the model picks which whop touch credits the entity and which non-whop source wins otherwise.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.adCampaigns.delete(id) -> DeleteAdCampaignsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Deletes an ad campaign and archives it on the ad platform (cascades to ad groups and ads).
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.adCampaigns().delete(
    "id",
    DeleteAdCampaignsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ad campaign ID.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.adCampaigns.update(id, request) -> AdCampaign</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Updates an ad campaign's editable fields (title, budget, schedule, bid strategy, special ad categories, and, before launch, budget optimization), and launches a draft campaign by setting status to active. Objective, budget type and desired cost per result are fixed at creation and cannot be changed.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.adCampaigns().update(
    "id",
    UpdateAdCampaignsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ad campaign ID.
    
</dd>
</dl>

<dl>
<dd>

**bidType:** `Optional<UpdateAdCampaignsRequestBidType>` — How delivery bids in the ad auction: `minimum_cost` gets the most results for the budget, `average_target` holds an average cost per result, `maximum_target` never bids above a cap. Switching to `minimum_cost` clears the cap amounts stored on the campaign's ad groups. Only for campaigns that own the budget.
    
</dd>
</dl>

<dl>
<dd>

**budgetAmount:** `Optional<Double>` — The campaign budget, in the account's currency. Interpreted as daily or lifetime per the campaign's existing budget type.
    
</dd>
</dl>

<dl>
<dd>

**budgetOptimization:** `Optional<UpdateAdCampaignsRequestBudgetOptimization>` — Which level owns the budget: the whole campaign (`ad_campaign`) or each ad group individually (`ad_group`). Only changeable before the campaign is live on the ad network; switching to `ad_campaign` requires budget_amount in the same request, and switching to `ad_group` clears the campaign budget.
    
</dd>
</dl>

<dl>
<dd>

**endsAt:** `Optional<String>` — When the campaign stops delivering, as an ISO 8601 timestamp. Only for campaigns that own the budget.
    
</dd>
</dl>

<dl>
<dd>

**specialAdCategories:** `Optional<List<UpdateAdCampaignsRequestSpecialAdCategoriesItem>>` — Regulated categories the campaign falls under. Editable on any campaign, draft or launched; pass an empty array to clear.
    
</dd>
</dl>

<dl>
<dd>

**startsAt:** `Optional<String>` — When the campaign starts delivering, as an ISO 8601 timestamp. Only for campaigns that own the budget.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<UpdateAdCampaignsRequestStatus>` — Set to active to launch a draft campaign (moderates and pushes it live). Live-campaign pause and resume use the pause and unpause actions.
    
</dd>
</dl>

<dl>
<dd>

**title:** `Optional<String>` — The name of the campaign.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.adCampaigns.duplicate(id, request) -> DuplicateAdCampaignsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Creates copies of the campaign in `duplicating` status and returns them; each copy transitions to `draft` once duplication completes. Poll each returned campaign until it leaves `duplicating` — a copy that could not be completed is deleted and returns 404.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.adCampaigns().duplicate(
    "id",
    DuplicateAdCampaignsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ad campaign ID.
    
</dd>
</dl>

<dl>
<dd>

**count:** `Optional<Integer>` — Number of copies to create (1-10). Defaults to 1.
    
</dd>
</dl>

<dl>
<dd>

**preserveEngagement:** `Optional<Boolean>` — Whether the copied ads keep the original posts' engagement (likes, comments, shares). Defaults to false.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.adCampaigns.pause(id) -> AdCampaign</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Pauses an active ad campaign.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.adCampaigns().pause(
    "id",
    PauseAdCampaignsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ad campaign ID.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.adCampaigns.retryPayment(id) -> AdCampaign</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retries billing for an ad campaign whose payment previously failed.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.adCampaigns().retryPayment(
    "id",
    RetryPaymentAdCampaignsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ad campaign ID.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.adCampaigns.unpause(id) -> AdCampaign</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Resumes a paused ad campaign.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.adCampaigns().unpause(
    "id",
    UnpauseAdCampaignsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ad campaign ID.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Ad Groups
<details><summary><code>client.adGroups.list() -> SyncPagingIterable&amp;lt;AdGroup&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists ad groups for the account, newest first.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.adGroups().list(
    ListAdGroupsRequest
        .builder()
        .adCampaignIds(
            Arrays.asList("adcamp_xxxxxxxxxxxxxx")
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — Account whose ad groups to list. Defaults to the authenticated account.
    
</dd>
</dl>

<dl>
<dd>

**adCampaignId:** `Optional<String>` — Filter to ad groups in this campaign.
    
</dd>
</dl>

<dl>
<dd>

**adCampaignIds:** `Optional<String>` — Filter to ad groups in these campaigns (max 100). Repeat the parameter for each id (ad_campaign_ids=a&ad_campaign_ids=b).
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<ListAdGroupsRequestStatus>` — Filter to ad groups with this status.
    
</dd>
</dl>

<dl>
<dd>

**query:** `Optional<String>` — Filter ad groups by a title or ID substring.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListAdGroupsRequestOrder>` — The field to sort by. Defaults to created_at. Stat columns (spend, impressions, …) rank over the stats_from/stats_to window across the whole list, not just the current page. results, cost_per_result and return_on_ad_spend rank by the same Whop pixel-attributed values the response reports.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListAdGroupsRequestDirection>` — The sort direction. Defaults to desc.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Only return ad groups created before this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Only return ad groups created after this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**statsFrom:** `Optional<String>` — Start of the stats window. Defaults to all-time.
    
</dd>
</dl>

<dl>
<dd>

**statsTo:** `Optional<String>` — End of the stats window. Defaults to now.
    
</dd>
</dl>

<dl>
<dd>

**timeZone:** `Optional<String>` — IANA timezone (e.g. America/New_York) the stats window is interpreted in. Bare stats_from/stats_to dates resolve to day boundaries on this clock. Defaults to UTC.
    
</dd>
</dl>

<dl>
<dd>

**attributionModel:** `Optional<ListAdGroupsRequestAttributionModel>` — Attribution model the conversion stats count under (defaults to last_touch). Under both models a journey with any whop ad touch attributes to whop; the model picks which whop touch credits the entity and which non-whop source wins otherwise.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of ad groups to return.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor to fetch the page after (from page_info.end_cursor).
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — The number of ad groups to return from the end of the range.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Cursor to fetch the page before (from page_info.start_cursor).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.adGroups.create(request) -> AdGroup</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Creates an ad group (ad set) in a campaign.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.adGroups().create(
    CreateAdGroupsRequest
        .builder()
        .adCampaignId("adcamp_xxxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**adCampaignId:** `String` — The ad campaign to create the ad group in, prefixed `adcamp_`.
    
</dd>
</dl>

<dl>
<dd>

**audiences:** `Optional<AdGroupAudiencesBody>` — Saved audiences to deliver to or exclude. Can't be combined with demographics.automatic.
    
</dd>
</dl>

<dl>
<dd>

**bidType:** `Optional<CreateAdGroupsRequestBidType>` — How delivery bids are set in the ad auction. Target-based strategies use `desired_cost_per_result`.
    
</dd>
</dl>

<dl>
<dd>

**budgetAmount:** `Optional<Double>` — This ad group's budget, in the ad account's currency. Omit when the budget is set on the campaign instead.
    
</dd>
</dl>

<dl>
<dd>

**budgetType:** `Optional<CreateAdGroupsRequestBudgetType>` — Whether budget_amount is spent per day (`daily`) or over the ad group's full run (`lifetime`).
    
</dd>
</dl>

<dl>
<dd>

**conversionEvent:** `Optional<ConversionEvent>` 
    
</dd>
</dl>

<dl>
<dd>

**conversionLocation:** `Optional<CreateAdGroupsRequestConversionLocation>` — Where the outcome being optimized for occurs, such as a website visit, social-profile visit, messaging conversation, ad interaction, or lead-form submission. The lead form itself is set on the ad.
    
</dd>
</dl>

<dl>
<dd>

**demographics:** `Optional<AdGroupDemographicsBody>` — Age, gender, and automatic-audience targeting.
    
</dd>
</dl>

<dl>
<dd>

**desiredCostPerResult:** `Optional<Double>` — Cost per result to aim for (`average_target`) or never exceed (`maximum_target`).
    
</dd>
</dl>

<dl>
<dd>

**detailedTargeting:** `Optional<AdGroupDetailedTargetingBody>` — Interest, behavior, and demographic targeting, using categories from the ad platform's targeting taxonomy. Entries across interests, behaviors, and demographics are OR'd together (anyone matching any entry is reached), matching Ads Manager's detailed-targeting box. At most 100 entries per section. Can't be combined with demographics.automatic, and unavailable to campaigns with special_ad_categories. Send the complete intended state — a section you omit is cleared.
    
</dd>
</dl>

<dl>
<dd>

**devices:** `Optional<AdGroupDevicesBody>` — Device platforms and operating systems to target.
    
</dd>
</dl>

<dl>
<dd>

**dynamicCreative:** `Optional<Boolean>` — Let the ad platform automatically mix and match this ad group's creatives and copy to find the best-performing combinations. Set at creation; can't be changed afterward.
    
</dd>
</dl>

<dl>
<dd>

**endsAt:** `Optional<String>` — When the ad group stops delivering, as an ISO 8601 timestamp. Omit to run until paused.
    
</dd>
</dl>

<dl>
<dd>

**frequencyCap:** `Optional<CreateAdGroupsRequestFrequencyCap>` — Cap on how often one person sees ads from this ad group. Only available on campaigns with the `awareness` objective.
    
</dd>
</dl>

<dl>
<dd>

**languages:** `Optional<List<String>>` — Languages to target, as ISO 639 codes such as `en` or `es`. Empty or omitted targets all languages.
    
</dd>
</dl>

<dl>
<dd>

**messageApps:** `Optional<List<CreateAdGroupsRequestMessageAppsItem>>` — Apps the conversation opens in. Required when setting `conversion_location` to `messaging`, and rejected unless the ad group's conversion location is `messaging`.
    
</dd>
</dl>

<dl>
<dd>

**minimumDailySpend:** `Optional<Double>` — Minimum the ad group tries to spend each day.
    
</dd>
</dl>

<dl>
<dd>

**optimizationGoal:** `Optional<CreateAdGroupsRequestOptimizationGoal>` — The result the ad group's delivery is optimized to get the most of.
    
</dd>
</dl>

<dl>
<dd>

**placements:** `Optional<CreateAdGroupsRequestPlacements>` 

`automatic` to let the ad platform choose placements, or the list of platforms and positions to target. Omit a platform's positions to target all of them.

Valid positions per platform:

- `facebook`: `feed`, `right_hand_column`, `marketplace`, `search`, `profile_feed`, `notification`, `story`, `instream_video`, `facebook_reels`, `facebook_reels_overlay`, `biz_disco_feed`
- `instagram`: `stream`, `story`, `explore`, `explore_home`, `reels`, `profile_feed`, `profile_reels`, `ig_search`
- `messenger`: `story`
- `audience_network`: `classic`, `rewarded_video`
- `threads`: `threads_stream`
- `whatsapp`: `status`
    
</dd>
</dl>

<dl>
<dd>

**regions:** `Optional<AdGroupRegionsBody>` — Locations to target and exclude.
    
</dd>
</dl>

<dl>
<dd>

**startsAt:** `Optional<String>` — When the ad group starts delivering, as an ISO 8601 timestamp. Omit to start as soon as it's active.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<CreateAdGroupsRequestStatus>` — Initial status (default: `active`).
    
</dd>
</dl>

<dl>
<dd>

**title:** `Optional<String>` — The display name of the ad group.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.adGroups.estimateReach(request) -> ReachEstimate</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Estimates how many people a draft targeting spec can reach, before an ad group is created. The body takes the same targeting fields as creating an ad group — `regions`, `demographics`, `detailed_targeting`, `audiences`, `languages`, and `devices` — and nothing is persisted.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.adGroups().estimateReach(
    EstimateReachAdGroupsRequest
        .builder()
        .platform(EstimateReachAdGroupsRequestPlatform.META)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — Account to estimate on behalf of. Defaults to the authenticated account.
    
</dd>
</dl>

<dl>
<dd>

**audiences:** `Optional<AdGroupAudiencesBody>` — Saved audiences to deliver to or exclude. Can't be combined with demographics.automatic.
    
</dd>
</dl>

<dl>
<dd>

**demographics:** `Optional<AdGroupDemographicsBody>` — Age, gender, and automatic-audience targeting.
    
</dd>
</dl>

<dl>
<dd>

**detailedTargeting:** `Optional<AdGroupDetailedTargetingBody>` — Interest, behavior, and demographic targeting, using categories from the ad platform's targeting taxonomy. At most 100 entries per section.
    
</dd>
</dl>

<dl>
<dd>

**devices:** `Optional<AdGroupDevicesBody>` — Device platforms and operating systems to target.
    
</dd>
</dl>

<dl>
<dd>

**languages:** `Optional<List<String>>` — Languages to target, as ISO 639 codes such as `en` or `es`. Empty or omitted targets all languages.
    
</dd>
</dl>

<dl>
<dd>

**platform:** `EstimateReachAdGroupsRequestPlatform` — The ad network the estimate runs on.
    
</dd>
</dl>

<dl>
<dd>

**regions:** `Optional<AdGroupRegionsBody>` — Locations to target and exclude.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.adGroups.searchTargetingOptions() -> SearchTargetingOptionsAdGroupsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Searches the ad platform's targeting taxonomy for options to target an ad group with. Each result comes back in the exact shape the ad-group body accepts for its `type`, so it can be used in `detailed_targeting`, `regions`, or `languages` as-is. A blank `query` browses the small fixed lists (behaviors, browse demographic categories, languages); interests, work employers, job titles, schools, majors, and locations need a search term.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.adGroups().searchTargetingOptions(
    SearchTargetingOptionsAdGroupsRequest
        .builder()
        .platform(SearchTargetingOptionsAdGroupsRequestPlatform.META)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — Account to search on behalf of. Defaults to the authenticated account.
    
</dd>
</dl>

<dl>
<dd>

**platform:** `SearchTargetingOptionsAdGroupsRequestPlatform` — The ad network whose targeting taxonomy to search.
    
</dd>
</dl>

<dl>
<dd>

**query:** `Optional<String>` — The search term. Blank browses the fixed lists; interests, work employers, job titles, schools, majors, and locations return nothing without one.
    
</dd>
</dl>

<dl>
<dd>

**types:** `Optional<SearchTargetingOptionsAdGroupsRequestTypesItem>` — Kinds of targeting options to search. Defaults to all of them.
    
</dd>
</dl>

<dl>
<dd>

**locationTypes:** `Optional<SearchTargetingOptionsAdGroupsRequestLocationTypesItem>` — Narrow location results to these kinds of places. Only applies when `types` includes `locations`.
    
</dd>
</dl>

<dl>
<dd>

**country:** `Optional<String>` — Narrow location results to one country, as an ISO 3166-1 code such as `US`. Only applies when `types` includes `locations`.
    
</dd>
</dl>

<dl>
<dd>

**limit:** `Optional<Integer>` — Maximum number of results per requested type.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.adGroups.retrieve(id) -> AdGroup</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a single ad group.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.adGroups().retrieve(
    "id",
    RetrieveAdGroupsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ad group ID.
    
</dd>
</dl>

<dl>
<dd>

**statsFrom:** `Optional<String>` — Start of the stats window.
    
</dd>
</dl>

<dl>
<dd>

**statsTo:** `Optional<String>` — End of the stats window.
    
</dd>
</dl>

<dl>
<dd>

**timeZone:** `Optional<String>` — IANA timezone the stats window is interpreted in. Defaults to UTC.
    
</dd>
</dl>

<dl>
<dd>

**attributionModel:** `Optional<RetrieveAdGroupsRequestAttributionModel>` — Attribution model the conversion stats count under (defaults to last_touch). Under both models a journey with any whop ad touch attributes to whop; the model picks which whop touch credits the entity and which non-whop source wins otherwise.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.adGroups.delete(id) -> DeleteAdGroupsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Deletes an ad group.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.adGroups().delete(
    "id",
    DeleteAdGroupsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ad group ID.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.adGroups.update(id, request) -> AdGroup</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Updates an ad group's editable fields. Only the keys you send are changed.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.adGroups().update(
    "id",
    UpdateAdGroupsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ad group ID.
    
</dd>
</dl>

<dl>
<dd>

**audiences:** `Optional<AdGroupAudiencesBody>` — Saved audiences to deliver to or exclude. Can't be combined with demographics.automatic.
    
</dd>
</dl>

<dl>
<dd>

**bidType:** `Optional<UpdateAdGroupsRequestBidType>` — How delivery bids are set in the ad auction. Target-based strategies use `desired_cost_per_result`.
    
</dd>
</dl>

<dl>
<dd>

**budgetAmount:** `Optional<Double>` — This ad group's budget, in the ad account's currency. Omit when the budget is set on the campaign instead.
    
</dd>
</dl>

<dl>
<dd>

**budgetType:** `Optional<UpdateAdGroupsRequestBudgetType>` — Whether budget_amount is spent per day (`daily`) or over the ad group's full run (`lifetime`).
    
</dd>
</dl>

<dl>
<dd>

**conversionEvent:** `Optional<ConversionEvent>` 
    
</dd>
</dl>

<dl>
<dd>

**conversionLocation:** `Optional<UpdateAdGroupsRequestConversionLocation>` — Where the outcome being optimized for occurs, such as a website visit, social-profile visit, messaging conversation, ad interaction, or lead-form submission. The lead form itself is set on the ad.
    
</dd>
</dl>

<dl>
<dd>

**demographics:** `Optional<AdGroupDemographicsBody>` — Age, gender, and automatic-audience targeting.
    
</dd>
</dl>

<dl>
<dd>

**desiredCostPerResult:** `Optional<Double>` — Cost per result to aim for (`average_target`) or never exceed (`maximum_target`).
    
</dd>
</dl>

<dl>
<dd>

**detailedTargeting:** `Optional<AdGroupDetailedTargetingBody>` — Interest, behavior, and demographic targeting, using categories from the ad platform's targeting taxonomy. Entries across interests, behaviors, and demographics are OR'd together (anyone matching any entry is reached), matching Ads Manager's detailed-targeting box. At most 100 entries per section. Can't be combined with demographics.automatic, and unavailable to campaigns with special_ad_categories. Send the complete intended state — a section you omit is cleared.
    
</dd>
</dl>

<dl>
<dd>

**devices:** `Optional<AdGroupDevicesBody>` — Device platforms and operating systems to target.
    
</dd>
</dl>

<dl>
<dd>

**endsAt:** `Optional<String>` — When the ad group stops delivering, as an ISO 8601 timestamp. Omit to run until paused.
    
</dd>
</dl>

<dl>
<dd>

**frequencyCap:** `Optional<UpdateAdGroupsRequestFrequencyCap>` — Cap on how often one person sees ads from this ad group. Only available on campaigns with the `awareness` objective.
    
</dd>
</dl>

<dl>
<dd>

**languages:** `Optional<List<String>>` — Languages to target, as ISO 639 codes such as `en` or `es`. Empty or omitted targets all languages.
    
</dd>
</dl>

<dl>
<dd>

**messageApps:** `Optional<List<UpdateAdGroupsRequestMessageAppsItem>>` — Apps the conversation opens in. Required when setting `conversion_location` to `messaging`, and rejected unless the ad group's conversion location is `messaging`.
    
</dd>
</dl>

<dl>
<dd>

**minimumDailySpend:** `Optional<Double>` — Minimum the ad group tries to spend each day.
    
</dd>
</dl>

<dl>
<dd>

**optimizationGoal:** `Optional<UpdateAdGroupsRequestOptimizationGoal>` — The result the ad group's delivery is optimized to get the most of.
    
</dd>
</dl>

<dl>
<dd>

**placements:** `Optional<UpdateAdGroupsRequestPlacements>` 

`automatic` to let the ad platform choose placements, or the list of platforms and positions to target. Omit a platform's positions to target all of them.

Valid positions per platform:

- `facebook`: `feed`, `right_hand_column`, `marketplace`, `search`, `profile_feed`, `notification`, `story`, `instream_video`, `facebook_reels`, `facebook_reels_overlay`, `biz_disco_feed`
- `instagram`: `stream`, `story`, `explore`, `explore_home`, `reels`, `profile_feed`, `profile_reels`, `ig_search`
- `messenger`: `story`
- `audience_network`: `classic`, `rewarded_video`
- `threads`: `threads_stream`
- `whatsapp`: `status`
    
</dd>
</dl>

<dl>
<dd>

**regions:** `Optional<AdGroupRegionsBody>` — Locations to target and exclude.
    
</dd>
</dl>

<dl>
<dd>

**startsAt:** `Optional<String>` — When the ad group starts delivering, as an ISO 8601 timestamp. Omit to start as soon as it's active.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<UpdateAdGroupsRequestStatus>` — Initial status (default: `active`).
    
</dd>
</dl>

<dl>
<dd>

**title:** `Optional<String>` — The display name of the ad group.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.adGroups.duplicate(id, request) -> DuplicateAdGroupsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Creates copies of the ad group in `duplicating` status and returns them — into its own campaign, or into target_ad_campaign_id (which must belong to the same account and be compatible with the ad group's targeting and goals); each copy transitions to its final status (matching the source's active/paused state) once duplication completes. Poll each returned ad group until it leaves `duplicating` — a copy that could not be completed is deleted and returns 404.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.adGroups().duplicate(
    "id",
    DuplicateAdGroupsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ad group ID.
    
</dd>
</dl>

<dl>
<dd>

**count:** `Optional<Integer>` — Number of copies to create (1-10). Defaults to 1.
    
</dd>
</dl>

<dl>
<dd>

**preserveEngagement:** `Optional<Boolean>` — Whether the copied ads keep the original posts' engagement (likes, comments, shares). Defaults to false.
    
</dd>
</dl>

<dl>
<dd>

**targetAdCampaignId:** `Optional<String>` — Campaign to duplicate into. Defaults to the ad group's own campaign.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.adGroups.pause(id) -> AdGroup</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Pauses delivery of an ad group.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.adGroups().pause(
    "id",
    PauseAdGroupsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ad group ID.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.adGroups.unpause(id) -> AdGroup</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Resumes delivery of a paused ad group.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.adGroups().unpause(
    "id",
    UnpauseAdGroupsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ad group ID.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## AdReports
<details><summary><code>client.adReports.retrieve() -> AdReport</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Performance report for a company, ad campaigns, ad groups, or ads. Always returns aggregate `summary` totals summed across the scope. Set `granularity` to additionally get a time series, or set `breakdown` (`campaign`/`ad_group`/`ad`) to additionally get per-entity rows inside the requested scope. Exactly one of `companyId`, `adCampaignIds`, `adGroupIds`, or `adIds` must be provided.

Required permissions:
 - `ad_campaign:stats:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.adReports().retrieve(
    RetrieveAdReportsRequest
        .builder()
        .from(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .to(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .companyId("biz_xxxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**adCampaignIds:** `Optional<String>` — Scope the report to these ad campaigns (max 100); stats are summed across them. Mutually exclusive with `companyId`, `adGroupIds`, and `adIds`.
    
</dd>
</dl>

<dl>
<dd>

**adGroupIds:** `Optional<String>` — Scope the report to these ad groups (max 100); stats are summed across them. Mutually exclusive with `companyId`, `adCampaignIds`, and `adIds`.
    
</dd>
</dl>

<dl>
<dd>

**adIds:** `Optional<String>` — Scope the report to these ads (max 100); stats are summed across them. Mutually exclusive with `companyId`, `adCampaignIds`, and `adGroupIds`.
    
</dd>
</dl>

<dl>
<dd>

**breakdown:** `Optional<AdReportBreakdownLevels>` 
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `Optional<String>` — The unique identifier of a company. Mutually exclusive with `adCampaignIds`, `adGroupIds`, and `adIds`. Use with `breakdown` to fan out across every campaign, ad group, or ad in the company without paging.
    
</dd>
</dl>

<dl>
<dd>

**currency:** `Optional<String>` — ISO 4217 currency code to report `spend` in. Defaults to the company's ads reporting currency.
    
</dd>
</dl>

<dl>
<dd>

**from:** `OffsetDateTime` — Inclusive start of the reporting window.
    
</dd>
</dl>

<dl>
<dd>

**granularity:** `Optional<Granularities>` 
    
</dd>
</dl>

<dl>
<dd>

**to:** `OffsetDateTime` — Inclusive end of the reporting window.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Ads
<details><summary><code>client.ads.list() -> SyncPagingIterable&amp;lt;Ad&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the ads for an account, with stats over the requested window.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.ads().list(
    ListAdsRequest
        .builder()
        .adCampaignIds(
            Arrays.asList("adcamp_xxxxxxxxxxxxxx")
        )
        .adGroupIds(
            Arrays.asList("adgrp_xxxxxxxxxxxxxx")
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The account the ads belong to. Defaults to the account-scoped key's own account.
    
</dd>
</dl>

<dl>
<dd>

**adCampaignId:** `Optional<String>` — Only return ads in this ad campaign.
    
</dd>
</dl>

<dl>
<dd>

**adCampaignIds:** `Optional<String>` — Only return ads in these ad campaigns (max 100). Repeat the parameter for each id (ad_campaign_ids=a&ad_campaign_ids=b).
    
</dd>
</dl>

<dl>
<dd>

**adGroupId:** `Optional<String>` — Only return ads in this ad group.
    
</dd>
</dl>

<dl>
<dd>

**adGroupIds:** `Optional<String>` — Only return ads in these ad groups (max 100). Repeat the parameter for each id (ad_group_ids=a&ad_group_ids=b).
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<ListAdsRequestStatus>` — Only return ads with this status.
    
</dd>
</dl>

<dl>
<dd>

**query:** `Optional<String>` — Filter ads by a title or ID substring.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListAdsRequestOrder>` — The field to sort by. Defaults to created_at. Stat columns (spend, impressions, …) rank over the stats_from/stats_to window across the whole list, not just the current page. results, cost_per_result and return_on_ad_spend rank by the same Whop pixel-attributed values the response reports.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListAdsRequestDirection>` — The sort direction. Defaults to desc.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Only return ads created before this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Only return ads created after this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**statsFrom:** `Optional<String>` — Start of the stats window. Defaults to all-time.
    
</dd>
</dl>

<dl>
<dd>

**statsTo:** `Optional<String>` — End of the stats window. Defaults to now.
    
</dd>
</dl>

<dl>
<dd>

**timeZone:** `Optional<String>` — IANA timezone (e.g. America/New_York) the stats window is interpreted in. Bare stats_from/stats_to dates resolve to day boundaries on this clock. Defaults to UTC.
    
</dd>
</dl>

<dl>
<dd>

**attributionModel:** `Optional<ListAdsRequestAttributionModel>` — Attribution model the conversion stats count under (defaults to last_touch). Under both models a journey with any whop ad touch attributes to whop; the model picks which whop touch credits the entity and which non-whop source wins otherwise.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of ads to return.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor to fetch the page after (from page_info.end_cursor).
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — The number of ads to return from the end of the range.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Cursor to fetch the page before (from page_info.start_cursor).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.ads.create(request) -> Ad</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Creates an ad in an ad group.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.ads().create(
    CreateAdsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**adGroup:** `Optional<Map<String, Object>>` — An inline ad group to create (same shape as POST /ad_groups, including ad_campaign_id). Creates the ad group and the ad together. Provide this OR ad_group_id.
    
</dd>
</dl>

<dl>
<dd>

**adGroupId:** `Optional<String>` — The existing ad group to create the ad in. Provide this OR ad_group, not both.
    
</dd>
</dl>

<dl>
<dd>

**callToAction:** `Optional<CreateAdsRequestCallToAction>` — The call-to-action button shown on the ad.
    
</dd>
</dl>

<dl>
<dd>

**creatives:** `Optional<List<CreateAdsRequestCreativesItem>>` — The ad's creative assets. Each entry is an uploaded file id with an optional format; omit format for the original asset. Two or more entries with no format become a carousel (2-10 attachments), in order, sharing the ad's copy.
    
</dd>
</dl>

<dl>
<dd>

**descriptions:** `Optional<List<String>>` — The description variants shown on the ad.
    
</dd>
</dl>

<dl>
<dd>

**headlines:** `Optional<List<String>>` — The headline variants shown on the ad.
    
</dd>
</dl>

<dl>
<dd>

**leadForm:** `Optional<CreateAdsRequestLeadForm>` — Instant lead form for the ad. Only allowed when the ad group's conversion_location is an instant-form destination (instant_forms, instant_forms_and_messenger, website_and_instant_forms). Mutually exclusive with lead_form_id.
    
</dd>
</dl>

<dl>
<dd>

**leadFormId:** `Optional<String>` — Use an existing instant form instead of creating one — the form's platform ID, from a form already on the ad's Facebook page. Only allowed when the ad group's conversion_location is an instant-form destination. Mutually exclusive with lead_form.
    
</dd>
</dl>

<dl>
<dd>

**messagingConfig:** `Optional<CreateAdsRequestMessagingConfig>` — Click-to-message welcome copy: the greeting (message) and the ice-breaker prompt (keyword).
    
</dd>
</dl>

<dl>
<dd>

**multiAdvertiserAds:** `Optional<Boolean>` — Whether the ad can appear alongside other advertisers' ads in the same unit. Defaults to true.
    
</dd>
</dl>

<dl>
<dd>

**postId:** `Optional<String>` — Promote an existing post instead of uploading creatives — a Facebook post or Instagram media id. Mutually exclusive with creatives. Pair with post_source.
    
</dd>
</dl>

<dl>
<dd>

**postSource:** `Optional<CreateAdsRequestPostSource>` — Identifies the network that owns `post_id`. The source is inferred from the ID shape when omitted.
    
</dd>
</dl>

<dl>
<dd>

**primaryTexts:** `Optional<List<String>>` — The primary text variants shown in the ad body.
    
</dd>
</dl>

<dl>
<dd>

**socialAccounts:** `Optional<List<CreateAdsRequestSocialAccountsItem>>` — The social accounts the ad runs under — a connected Facebook page and, optionally, an Instagram profile.
    
</dd>
</dl>

<dl>
<dd>

**title:** `Optional<String>` — The display name of the ad.
    
</dd>
</dl>

<dl>
<dd>

**url:** `Optional<String>` — The URL the ad links to. Query parameters are merged into url_parameters, so the stored URL is always bare.
    
</dd>
</dl>

<dl>
<dd>

**urlParameters:** `Optional<Map<String, Object>>` — Query parameters to append to the destination URL, keyed by parameter name. Merged with any query string on `url`. Whop adds its own click-attribution parameters; those are reserved and rejected if you set them (utm_meta_ad_id, utm_meta_adset_id, utm_meta_campaign_id, utm_source, utm_placement, utm_medium, utm_content, utm_adset, utm_whop, wacid, wasid, waid, tw_source, tw_adid).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.ads.retrieve(id) -> Ad</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a single ad with stats over the requested window.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.ads().retrieve(
    "id",
    RetrieveAdsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ad ID.
    
</dd>
</dl>

<dl>
<dd>

**statsFrom:** `Optional<String>` — Start of the stats window.
    
</dd>
</dl>

<dl>
<dd>

**statsTo:** `Optional<String>` — End of the stats window.
    
</dd>
</dl>

<dl>
<dd>

**timeZone:** `Optional<String>` — IANA timezone the stats window is interpreted in. Defaults to UTC.
    
</dd>
</dl>

<dl>
<dd>

**attributionModel:** `Optional<RetrieveAdsRequestAttributionModel>` — Attribution model the conversion stats count under (defaults to last_touch). Under both models a journey with any whop ad touch attributes to whop; the model picks which whop touch credits the entity and which non-whop source wins otherwise.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.ads.delete(id) -> DeleteAdsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Deletes an ad.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.ads().delete(
    "id",
    DeleteAdsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ad ID.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.ads.update(id, request) -> Ad</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Updates an ad's editable fields.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.ads().update(
    "id",
    UpdateAdsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ad ID.
    
</dd>
</dl>

<dl>
<dd>

**callToAction:** `Optional<UpdateAdsRequestCallToAction>` — The call-to-action button shown on the ad.
    
</dd>
</dl>

<dl>
<dd>

**creatives:** `Optional<List<UpdateAdsRequestCreativesItem>>` — The ad's creative assets. Each entry is an uploaded file id with an optional format; omit format for the original asset. Replaces a live ad's creative on the platform. Two or more entries with no format replace it with a carousel (2-10 attachments), in order, sharing the ad's copy.
    
</dd>
</dl>

<dl>
<dd>

**descriptions:** `Optional<List<String>>` — The description variants shown on the ad.
    
</dd>
</dl>

<dl>
<dd>

**headlines:** `Optional<List<String>>` — The headline variants shown on the ad.
    
</dd>
</dl>

<dl>
<dd>

**leadForm:** `Optional<UpdateAdsRequestLeadForm>` — Instant lead form for the ad. Only allowed when the ad group's conversion_location is an instant-form destination (instant_forms, instant_forms_and_messenger, website_and_instant_forms). Mutually exclusive with lead_form_id.
    
</dd>
</dl>

<dl>
<dd>

**leadFormId:** `Optional<String>` — Use an existing instant form instead of creating one — the form's platform ID, from a form already on the ad's Facebook page. Only allowed when the ad group's conversion_location is an instant-form destination. Mutually exclusive with lead_form. Replaces a stored lead_form.
    
</dd>
</dl>

<dl>
<dd>

**messagingConfig:** `Optional<UpdateAdsRequestMessagingConfig>` — Click-to-message welcome copy: the greeting (message) and the ice-breaker prompt (keyword).
    
</dd>
</dl>

<dl>
<dd>

**multiAdvertiserAds:** `Optional<Boolean>` — Whether the ad can appear alongside other advertisers' ads in the same unit. Defaults to true.
    
</dd>
</dl>

<dl>
<dd>

**postId:** `Optional<String>` — Promote an existing post instead of uploading creatives — a Facebook post or Instagram media id. Mutually exclusive with creatives. Pair with post_source.
    
</dd>
</dl>

<dl>
<dd>

**postSource:** `Optional<UpdateAdsRequestPostSource>` — Identifies the network that owns `post_id`. The source is inferred from the ID shape when omitted.
    
</dd>
</dl>

<dl>
<dd>

**primaryTexts:** `Optional<List<String>>` — The primary text variants shown in the ad body.
    
</dd>
</dl>

<dl>
<dd>

**socialAccounts:** `Optional<List<UpdateAdsRequestSocialAccountsItem>>` — The social accounts the ad runs under — a connected Facebook page and, optionally, an Instagram profile.
    
</dd>
</dl>

<dl>
<dd>

**title:** `Optional<String>` — The display name of the ad.
    
</dd>
</dl>

<dl>
<dd>

**url:** `Optional<String>` — The URL the ad links to. Query parameters are merged into url_parameters, so the stored URL is always bare.
    
</dd>
</dl>

<dl>
<dd>

**urlParameters:** `Optional<Map<String, Object>>` — Query parameters to append to the destination URL, keyed by parameter name. Merged with any query string on `url`. Whop adds its own click-attribution parameters; those are reserved and rejected if you set them (utm_meta_ad_id, utm_meta_adset_id, utm_meta_campaign_id, utm_source, utm_placement, utm_medium, utm_content, utm_adset, utm_whop, wacid, wasid, waid, tw_source, tw_adid).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.ads.duplicate(id, request) -> DuplicateAdsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Copies the ad into its own ad group, or into target_ad_group_id (which must belong to the same account and be compatible with the ad). Copies keep the source ad's active/paused state.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.ads().duplicate(
    "id",
    DuplicateAdsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ad ID.
    
</dd>
</dl>

<dl>
<dd>

**count:** `Optional<Integer>` — Number of copies to create (1-10). Defaults to 1.
    
</dd>
</dl>

<dl>
<dd>

**preserveEngagement:** `Optional<Boolean>` — Whether the copies keep the original post's engagement (likes, comments, shares). Defaults to false.
    
</dd>
</dl>

<dl>
<dd>

**targetAdGroupId:** `Optional<String>` — Ad group to duplicate into. Defaults to the ad's own ad group.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.ads.pause(id) -> Ad</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Pauses an active ad.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.ads().pause(
    "id",
    PauseAdsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ad ID.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.ads.unpause(id) -> Ad</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Resumes a paused ad.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.ads().unpause(
    "id",
    UnpauseAdsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ad ID.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Affiliates
<details><summary><code>client.affiliates.list() -> SyncPagingIterable&amp;lt;AffiliateListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of affiliates for the actor in context, with optional filtering by status, search, and sorting.

Required permissions:
 - `affiliate:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.affiliates().list(
    ListAffiliatesRequest
        .builder()
        .companyId("biz_xxxxxxxxxxxxxx")
        .first(42)
        .last(42)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `String` — The unique identifier of the company to list affiliates for.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<Direction>` 
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<AffiliatesSortableColumns>` 
    
</dd>
</dl>

<dl>
<dd>

**query:** `Optional<String>` — Search affiliates by username.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<Status>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.affiliates.create(request) -> Affiliate</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Creates or finds an affiliate for a company and user.

Required permissions:
 - `affiliate:create`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.affiliates().create(
    CreateAffiliatesRequest
        .builder()
        .companyId("biz_xxxxxxxxxxxxxx")
        .userIdentifier("user_identifier")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**companyId:** `String` — The ID of the company to create the affiliate for.
    
</dd>
</dl>

<dl>
<dd>

**userIdentifier:** `String` — The user identifier (username, email, user ID, or Discord ID).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.affiliates.retrieve(id) -> Affiliate</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing affiliate.

Required permissions:
 - `affiliate:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.affiliates().retrieve(
    "aff_xxxxxxxxxxxxxx",
    RetrieveAffiliatesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the affiliate.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.affiliates.archive(id) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Archives an existing Affiliate

Required permissions:
 - `affiliate:update`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.affiliates().archive(
    "aff_xxxxxxxxxxxxxx",
    ArchiveAffiliatesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The internal ID of the affiliate to archive.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.affiliates.unarchive(id) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Unarchives an existing Affiliate

Required permissions:
 - `affiliate:update`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.affiliates().unarchive(
    "aff_xxxxxxxxxxxxxx",
    UnarchiveAffiliatesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The internal ID of the affiliate to archive.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## AiChats
<details><summary><code>client.aiChats.list() -> SyncPagingIterable&amp;lt;AiChatListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of AI chat threads for the current authenticated user.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.aiChats().list(
    ListAiChatsRequest
        .builder()
        .first(42)
        .last(42)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**onlyActiveCrons:** `Optional<Boolean>` — When true, returns only chats with an active cron schedule
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.aiChats.create(request) -> AiChat</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Create a new AI chat thread and send the first message to the AI agent.

Required permissions:
 - `ai_chat:create`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.aiChats().create(
    CreateAiChatsRequest
        .builder()
        .messageText("message_text")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**currentCompanyId:** `Optional<String>` — The unique identifier of the company to set as context for the AI chat (e.g., "biz_XXXXX").
    
</dd>
</dl>

<dl>
<dd>

**messageAttachments:** `Optional<List<CreateAiChatsRequestMessageAttachmentsItem>>` — A list of previously uploaded file attachments to include with the first message.
    
</dd>
</dl>

<dl>
<dd>

**messageSource:** `Optional<AiChatMessageSourceTypes>` — The source of the message.
    
</dd>
</dl>

<dl>
<dd>

**messageText:** `String` — The text content of the first message to send to the AI agent.
    
</dd>
</dl>

<dl>
<dd>

**suggestionType:** `Optional<String>` — The type of suggestion prompt that was clicked, when message_source is 'suggestion'.
    
</dd>
</dl>

<dl>
<dd>

**title:** `Optional<String>` — An optional display title for the AI chat thread (e.g., "Help with billing").
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.aiChats.retrieve(id) -> AiChat</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing AI chat.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.aiChats().retrieve(
    "aich_xxxxxxxxxxxxx",
    RetrieveAiChatsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the AI chat to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.aiChats.delete(id) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Delete an AI chat thread so it no longer appears in the user's chat list.

Required permissions:
 - `ai_chat:delete`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.aiChats().delete(
    "aich_xxxxxxxxxxxxx",
    DeleteAiChatsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the AI chat to delete (e.g., "ai_chat_XXXXX").
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.aiChats.update(id, request) -> AiChat</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Update an AI chat's title, notification preferences, or associated company context.

Required permissions:
 - `ai_chat:update`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.aiChats().update(
    "aich_xxxxxxxxxxxxx",
    UpdateAiChatsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the AI chat to update (e.g., "ai_chat_XXXXX").
    
</dd>
</dl>

<dl>
<dd>

**currentCompanyId:** `Optional<String>` — The unique identifier of the company to set as context for the AI chat (e.g., "biz_XXXXX").
    
</dd>
</dl>

<dl>
<dd>

**notificationPreference:** `Optional<AiChatNotificationPreferences>` — The notification preference for the AI chat.
    
</dd>
</dl>

<dl>
<dd>

**title:** `Optional<String>` — The new display title for the AI chat thread (e.g., "Help with billing").
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## API Keys
<details><summary><code>client.apiKeys.list() -> SyncPagingIterable&amp;lt;ApiKey&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the API keys of an account or app, newest first. Responses never include the full secret — only its obfuscated form.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.apiKeys().list(
    ListApiKeysRequest
        .builder()
        .resourceId("resource_id")
        .resourceType(ListApiKeysRequestResourceType.ACCOUNT)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**resourceId:** `String` — The account (`biz_`) or app (`app_`) tag to list API keys for.
    
</dd>
</dl>

<dl>
<dd>

**resourceType:** `ListApiKeysRequestResourceType` — The type of resource that owns the API keys.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<ListApiKeysRequestCreatedBefore>` — Only return API keys created before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<ListApiKeysRequestCreatedAfter>` — Only return API keys created after this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of API keys to return (default 20, max 100).
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor; returns API keys after this position.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — The number of API keys to return from the end of the range.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — A cursor; returns API keys before this position.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListApiKeysRequestOrder>` — The field to sort API keys by.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListApiKeysRequestDirection>` — Sort direction.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.apiKeys.create(request) -> ApiKey</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Creates an API key for an account or app. The response is the only place the full `secret_key` is returned — store it immediately. Requires a user session; API keys cannot manage API keys.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.apiKeys().create(
    CreateApiKeysRequest
        .builder()
        .name("Shine Time Booking (production)")
        .permissions(
            CreateApiKeysRequestPermissions
                .builder()
                .build()
        )
        .resourceId("biz_xxxxxxxxxxxxxx")
        .resourceType(CreateApiKeysRequestResourceType.ACCOUNT)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**apiVersionDate:** `Optional<CreateApiKeysRequestApiVersionDate>` — Dated API version used when requests authenticated with this key omit the `Api-Version-Date` header. New keys default to the latest version.
    
</dd>
</dl>

<dl>
<dd>

**expiresAt:** `Optional<String>` — When the API key should stop working, as an ISO 8601 timestamp. Omit (or pass `null` on update) for a key that never expires.
    
</dd>
</dl>

<dl>
<dd>

**ipAllowlist:** `Optional<List<String>>` — IPv4/IPv6 CIDR ranges allowed to use this key, for example `["203.0.113.0/24"]`. Empty or `null` allows any IP.
    
</dd>
</dl>

<dl>
<dd>

**name:** `String` — A human-readable name for the API key, such as 'Production API Key'.
    
</dd>
</dl>

<dl>
<dd>

**permissions:** `CreateApiKeysRequestPermissions` — The permissions policy for the API key: explicit permission statements, or a system role to inherit from. Statements without a `resources` array default to the owning account (Account API keys) or every key-addressable resource (App API keys).
    
</dd>
</dl>

<dl>
<dd>

**resourceId:** `String` — The account (`biz_`) or app (`app_`) tag to create the API key for.
    
</dd>
</dl>

<dl>
<dd>

**resourceType:** `CreateApiKeysRequestResourceType` — The type of resource that will own this API key.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.apiKeys.listPermissions() -> ListPermissionsApiKeysResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the catalog of permission actions that can be granted to users, apps, and API keys — the source for the dashboard's permission pickers. Small and returned in full on one page.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.apiKeys().listPermissions();
```
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.apiKeys.retrieve(id) -> ApiKey</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves an API key with its effective permission grants. The full secret is never returned — rotate the key if it was lost.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.apiKeys().retrieve(
    "id",
    RetrieveApiKeysRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — API key ID, prefixed `apik_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.apiKeys.delete(id) -> DeleteApiKeysResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Permanently revokes an API key; requests using its secret stop authenticating immediately. Default and agent-backend keys cannot be deleted.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.apiKeys().delete(
    "id",
    DeleteApiKeysRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — API key ID, prefixed `apik_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.apiKeys.update(id, request) -> ApiKey</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Updates an API key's name, permissions, API version, expiration, or IP allowlist. Fields that are omitted keep their current value; default keys cannot be modified.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.apiKeys().update(
    "id",
    UpdateApiKeysRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — API key ID, prefixed `apik_`.
    
</dd>
</dl>

<dl>
<dd>

**apiVersionDate:** `Optional<UpdateApiKeysRequestApiVersionDate>` — Dated API version used when requests authenticated with this key omit the `Api-Version-Date` header. New keys default to the latest version.
    
</dd>
</dl>

<dl>
<dd>

**expiresAt:** `Optional<String>` — When the API key should stop working, as an ISO 8601 timestamp. Omit (or pass `null` on update) for a key that never expires.
    
</dd>
</dl>

<dl>
<dd>

**ipAllowlist:** `Optional<List<String>>` — IPv4/IPv6 CIDR ranges allowed to use this key, for example `["203.0.113.0/24"]`. Empty or `null` allows any IP.
    
</dd>
</dl>

<dl>
<dd>

**name:** `Optional<String>` — A new human-readable name for the API key.
    
</dd>
</dl>

<dl>
<dd>

**permissions:** `Optional<UpdateApiKeysRequestPermissions>` — The permissions policy for the API key: explicit permission statements, or a system role to inherit from. Statements without a `resources` array default to the owning account (Account API keys) or every key-addressable resource (App API keys).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.apiKeys.rotate(id) -> ApiKey</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Rotates the API key's secret, invalidating the previous secret immediately. The response is the only place the new `secret_key` is returned.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.apiKeys().rotate(
    "id",
    RotateApiKeysRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — API key ID, prefixed `apik_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## App Builds
<details><summary><code>client.appBuilds.list() -> SyncPagingIterable&amp;lt;AppBuild&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of build artifacts for an app, newest first, with optional platform, status, and creation-date filters.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.appBuilds().list(
    ListAppBuildsRequest
        .builder()
        .appId("app_id")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**appId:** `String` — The app to list builds for, prefixed `app_`.
    
</dd>
</dl>

<dl>
<dd>

**platform:** `Optional<ListAppBuildsRequestPlatform>` — Filter builds by target platform.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<ListAppBuildsRequestStatus>` — Filter builds by review status.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<ListAppBuildsRequestCreatedBefore>` — Only return builds created before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<ListAppBuildsRequestCreatedAfter>` — Only return builds created after this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of builds to return (default 20, max 100).
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor; returns builds after this position.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — The number of builds to return from the end of the range.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — A cursor; returns builds before this position.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.appBuilds.create(request) -> AppBuild</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Uploads a new build artifact for an app. Upload the file first (POST /files or a direct upload), then reference it here; iOS and Android take a .zip bundle, web takes a JavaScript file or a .zip archive of the hosted site.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.appBuilds().create(
    CreateAppBuildsRequest
        .builder()
        .attachment(
            CreateAppBuildsRequestAttachment
                .builder()
                .build()
        )
        .checksum("xxxxxxxxxxxxxxx")
        .platform(CreateAppBuildsRequestPlatform.IOS)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**aiPromptId:** `Optional<String>` — The AI prompt that generated this build, if applicable.
    
</dd>
</dl>

<dl>
<dd>

**appId:** `Optional<String>` — The app to create the build for, prefixed `app_`. Defaults to the app behind the presented credential.
    
</dd>
</dl>

<dl>
<dd>

**attachment:** `CreateAppBuildsRequestAttachment` — The uploaded build file: `{ id }` for an existing file or `{ direct_upload_id }` for a completed direct upload.
    
</dd>
</dl>

<dl>
<dd>

**checksum:** `String` — A client-generated checksum of the build file, used to verify file integrity when unpacked.
    
</dd>
</dl>

<dl>
<dd>

**platform:** `CreateAppBuildsRequestPlatform` — The target platform for the build.
    
</dd>
</dl>

<dl>
<dd>

**sourceAttachment:** `Optional<CreateAppBuildsRequestSourceAttachment>` — An optional compressed archive (.zip or .gz) of the source code that produced this build, stored alongside the build so it can be downloaded later. Referenced like `attachment`, and must be a different file.
    
</dd>
</dl>

<dl>
<dd>

**supportedAppViewTypes:** `Optional<List<CreateAppBuildsRequestSupportedAppViewTypesItem>>` — The view types this build supports. Only list the ones its code implements.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.appBuilds.retrieve(id) -> AppBuild</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing app build.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.appBuilds().retrieve(
    "id",
    RetrieveAppBuildsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — App build ID, prefixed `abld_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.appBuilds.promote(id) -> AppBuild</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Promotes a draft or approved app build to production so it becomes the active version served to users. Draft builds enter review first.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.appBuilds().promote(
    "id",
    PromoteAppBuildsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — App build ID, prefixed `abld_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Apps
<details><summary><code>client.apps.list() -> SyncPagingIterable&amp;lt;AppListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists apps on the Whop platform: the app store's live apps, or — with `account_id` and developer access to that account — every app the account owns. Requires authentication, except for the publicly readable lists: `verified_apps_only=true`, and `app_type=website` with no `account_id`, which returns every live deployed website that Whop has not verified — verified templates are the curated `verified_apps_only=true` list instead.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.apps().list(
    ListAppsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — Only return apps created by this account (`biz_` tag). With developer access to the account this includes its unlisted and hidden apps.
    
</dd>
</dl>

<dl>
<dd>

**appType:** `Optional<ListAppsRequestAppType>` — Filter apps by the type of end-user they are built for. Apps of type `website` are left out unless you ask for them by name.
    
</dd>
</dl>

<dl>
<dd>

**viewType:** `Optional<ListAppsRequestViewType>` — Only return apps supporting this view type, such as `dashboard` or `hub`.
    
</dd>
</dl>

<dl>
<dd>

**verifiedAppsOnly:** `Optional<Boolean>` — Whether to only return apps verified by Whop. Verified website templates — websites with a published web build — are included, even though websites are otherwise left out of app lists.
    
</dd>
</dl>

<dl>
<dd>

**query:** `Optional<String>` — A search string matched against app names.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListAppsRequestOrder>` — The field to sort apps by. Defaults to discoverable_at, showing the most recently published apps first. `template_usage` ranks Whop-verified apps first, then apps with a banner image, then by how many apps were created from each app as a template.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListAppsRequestDirection>` — Sort direction.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of apps to return (default 20, max 100).
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor; returns apps after this position.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — The number of apps to return from the end of the range.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — A cursor; returns apps before this position.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.apps.create(request) -> App</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Registers a new app on the Whop developer platform. Apps provide custom experiences that can be added to products.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.apps().create(
    CreateAppsRequest
        .builder()
        .name("Shine Time Booking")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The account to create the app for (`biz_` tag). Defaults to the account behind the presented credential.
    
</dd>
</dl>

<dl>
<dd>

**appType:** `Optional<CreateAppsRequestAppType>` — The type of app to create. Defaults to `b2c_app`.
    
</dd>
</dl>

<dl>
<dd>

**baseUrl:** `Optional<String>` — The base production URL where the app is hosted, such as `https://myapp.example.com`.
    
</dd>
</dl>

<dl>
<dd>

**icon:** `Optional<CreateAppsRequestIcon>` — The icon image for the app in PNG, JPEG, or GIF format, referencing an uploaded file: `{ id }` for an existing attachment or `{ direct_upload_id }` for a new direct upload.
    
</dd>
</dl>

<dl>
<dd>

**name:** `String` — The display name for the app, shown to users on the app store and product pages.
    
</dd>
</dl>

<dl>
<dd>

**redirectUris:** `Optional<List<String>>` — The whitelisted OAuth callback URLs that users are redirected to after authorizing the app.
    
</dd>
</dl>

<dl>
<dd>

**route:** `Optional<String>` — The subdomain route where the app's hosted web builds are served, such as `myapp` for myapp.whop.app.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.apps.updatePermissionsApp(appId, request) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Updates the permission requirements for an app

Required permissions:
 - `developer:update_app_authorization`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.apps().updatePermissionsApp(
    "app_id",
    UpdatePermissionsAppRequest
        .builder()
        .requestedPermissions(
            Arrays.asList(
                UpdatePermissionsAppRequestRequestedPermissionsItem
                    .builder()
                    .action("action")
                    .isRequired(true)
                    .justification("justification")
                    .build()
            )
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**appId:** `String` — The ID of the app the permission requirements are being updated for
    
</dd>
</dl>

<dl>
<dd>

**requestedPermissions:** `List<UpdatePermissionsAppRequestRequestedPermissionsItem>` — The permissions that the app will request off of users when a user installs the app.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.apps.retrieve(id) -> App</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves an app by ID, claimed route, or proxy domain id. Credential fields (api_key, default_api_key, secrets) render `null` unless the caller has the corresponding developer permission on the owning account.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.apps().retrieve(
    "id",
    RetrieveAppsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — App ID (prefixed `app_`), the app's claimed route, or its proxy domain id.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.apps.delete(id) -> DeleteAppsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Deletes an app. The app stops resolving within seconds — a website's site stops serving, and any claimed subdomain is reserved for a month before it can be claimed again.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.apps().delete(
    "id",
    DeleteAppsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — App ID (prefixed `app_`), the app's claimed route, or its proxy domain id.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.apps.update(id, request) -> App</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Updates the settings, metadata, or status of an app. Fields that are omitted keep their current value.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.apps().update(
    "id",
    UpdateAppsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — App ID (prefixed `app_`), the app's claimed route, or its proxy domain id.
    
</dd>
</dl>

<dl>
<dd>

**appStoreDescription:** `Optional<String>` — The detailed description shown on the app store's in-depth app view page.
    
</dd>
</dl>

<dl>
<dd>

**appType:** `Optional<UpdateAppsRequestAppType>` — The type of end-user the app is built for. Cannot be changed on an app whose type is already `website`.
    
</dd>
</dl>

<dl>
<dd>

**baseUrl:** `Optional<String>` — The base production URL where the app is hosted. Set to `null` to take the app proxy offline.
    
</dd>
</dl>

<dl>
<dd>

**dashboardPath:** `Optional<String>` — The URL path for the account dashboard view.
    
</dd>
</dl>

<dl>
<dd>

**description:** `Optional<String>` — A short description of the app shown in listings and search results.
    
</dd>
</dl>

<dl>
<dd>

**discoverPath:** `Optional<String>` — The URL path for the discover view.
    
</dd>
</dl>

<dl>
<dd>

**experiencePath:** `Optional<String>` — The URL path for the member-facing hub view, such as `/experiences/[experienceId]`.
    
</dd>
</dl>

<dl>
<dd>

**icon:** `Optional<UpdateAppsRequestIcon>` — The icon image for the app in PNG, JPEG, or GIF format, referencing an uploaded file: `{ id }` for an existing attachment or `{ direct_upload_id }` for a new direct upload.
    
</dd>
</dl>

<dl>
<dd>

**name:** `Optional<String>` — The display name for the app, shown to users on the app store and product pages.
    
</dd>
</dl>

<dl>
<dd>

**oauthClientType:** `Optional<UpdateAppsRequestOauthClientType>` — How the app authenticates at the OAuth token endpoint.
    
</dd>
</dl>

<dl>
<dd>

**openapiPath:** `Optional<String>` — The URL path to the app's OpenAPI spec file (requires the ai_chat capability).
    
</dd>
</dl>

<dl>
<dd>

**productionAndroidBuildId:** `Optional<String>` — The app build (`abld_` tag) to serve as the Android production build, or `null` to unassign it. Same rules as `production_web_build_id`.
    
</dd>
</dl>

<dl>
<dd>

**productionIosBuildId:** `Optional<String>` — The app build (`abld_` tag) to serve as the iOS production build, or `null` to unassign it. Same rules as `production_web_build_id`.
    
</dd>
</dl>

<dl>
<dd>

**productionWebBuildId:** `Optional<String>` — The app build (`abld_` tag) to serve as the web production build, or `null` to unassign it. The build must belong to this app, target web, and be in the draft or approved status; a draft build is queued for approval and takes over once approved. Requires the `developer:manage_builds` scope.
    
</dd>
</dl>

<dl>
<dd>

**redirectUris:** `Optional<List<String>>` — The whitelisted OAuth callback URLs users are redirected to after authorizing the app.
    
</dd>
</dl>

<dl>
<dd>

**requiredScopes:** `Optional<List<String>>` — The OAuth scopes the app requests from users when they install it.
    
</dd>
</dl>

<dl>
<dd>

**route:** `Optional<String>` — The subdomain route where the app's hosted web builds are served.
    
</dd>
</dl>

<dl>
<dd>

**secrets:** `Optional<Map<String, Object>>` — Secrets to add or overwrite on the app, as an object of string values. Keys not included are left untouched; pass null or an empty string as the value to delete a secret. Encrypted at rest and injected into the app's hosted server runtime.
    
</dd>
</dl>

<dl>
<dd>

**skillsPath:** `Optional<String>` — The URL path to the app's skills directory (requires the ai_chat capability).
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<UpdateAppsRequestStatus>` — Controls whether the app is published on Whop discovery or accessible only through its direct link. Publishing requires a name, icon, and description.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.apps.deploy(id, request) -> AppDeployment</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Builds the app's current source and ships it. Returns the run it started, so the caller can render progress from this response and then follow it on the app's `deployment` field. Only one deployment runs per app at a time — calling this while one is in flight reports that run rather than starting a second, and calling it with nothing to publish reports that instead of starting one.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.apps().deploy(
    "id",
    DeployAppsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The app to deploy, prefixed `app_`.
    
</dd>
</dl>

<dl>
<dd>

**draft:** `Optional<Boolean>` — Upload the build without making it live. Defaults to `false`, which deploys and promotes in one step.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.apps.logs(id) -> SyncPagingIterable&amp;lt;LogsAppsResponseDataItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists a hosted app's server runtime logs, most recent first: console output, uncaught exceptions, and failed-request summaries captured on whop.app hosting. Logs are retained for 7 days.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.apps().logs(
    "id",
    LogsAppsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ID of the app, which will look like app_*************.
    
</dd>
</dl>

<dl>
<dd>

**appBuildId:** `Optional<String>` — Only return logs from this build.
    
</dd>
</dl>

<dl>
<dd>

**level:** `Optional<LogsAppsRequestLevel>` — Only return console lines of this level.
    
</dd>
</dl>

<dl>
<dd>

**query:** `Optional<String>` — Only return logs whose message contains this text (case-insensitive).
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<OffsetDateTime>` — Start of the time window as an ISO 8601 timestamp. Defaults to 7 days before created_before.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<OffsetDateTime>` — End of the time window as an ISO 8601 timestamp. Defaults to now.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of log lines to return (max 500).
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor for fetching logs after a previous page.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — A cursor for fetching logs before a later page.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.apps.updatePermissions(id, request) -> App</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Replaces the set of permissions the app requests from users when they install it. Requires a user session: the `developer:update_app_authorization` scope cannot be delegated to API keys. Sensitive permissions require step-up verification.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.apps().updatePermissions(
    "id",
    UpdatePermissionsAppsRequest
        .builder()
        .requestedPermissions(
            Arrays.asList(
                UpdatePermissionsAppsRequestRequestedPermissionsItem
                    .builder()
                    .action("company:basic:read")
                    .isRequired(true)
                    .justification("Reads basic account info to render the dashboard home.")
                    .build()
            )
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — App ID, prefixed `app_`.
    
</dd>
</dl>

<dl>
<dd>

**requestedPermissions:** `List<UpdatePermissionsAppsRequestRequestedPermissionsItem>` — The full set of permissions the app requests on install; permissions not listed are removed.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Audiences
<details><summary><code>client.audiences.list() -> SyncPagingIterable&amp;lt;Audience&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists uploaded customer-list audiences for an account. Pass `audience_id` to return a specific audience.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.audiences().list(
    ListAudiencesRequest
        .builder()
        .accountId("account_id")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `String` — Account ID, prefixed `biz_`.
    
</dd>
</dl>

<dl>
<dd>

**audienceId:** `Optional<String>` — Audience ID, prefixed `adaud_`, used to filter the response to one audience.
    
</dd>
</dl>

<dl>
<dd>

**audienceType:** `Optional<ListAudiencesRequestAudienceType>` — Filter by audience type: `custom` (uploaded lists) or `lookalike`.
    
</dd>
</dl>

<dl>
<dd>

**sourceType:** `Optional<ListAudiencesRequestSourceType>` — Filter by member source: `csv_upload` (uploaded lists) or `people_filter` (automatic audiences built from saved People filters).
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Number of audiences to return. Defaults to 20; maximum 100.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor for the next page of audiences.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.audiences.create(request) -> CreateAudiencesResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Creates an audience. Default (`audience_type` omitted or `custom`): creates one audience from an uploaded customer identity CSV file (`name`, `column_mapping`, and `file_id` required) and starts processing it; responds with the audience object. With `filters`: creates an audience from saved People filters (`name` required) — membership is built from the account's People data, and `auto_refresh` decides whether it keeps tracking the filters or keeps whoever matched at creation. With `audience_type: lookalike`: creates a ladder of Meta lookalike audiences from an existing ready custom audience (`source_audience_id`, `count`, and `percentage` required) — `count` equal similarity bands slicing the top `percentage`% (3 audiences at 6% = 0–2%, 2–4%, 4–6%), each returned as its own audience in a `{ data: [...] }` envelope.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.audiences().create(
    CreateAudiencesRequest
        .builder()
        .accountId("biz_xxxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `String` — Account ID, prefixed `biz_`.
    
</dd>
</dl>

<dl>
<dd>

**audienceType:** `Optional<CreateAudiencesRequestAudienceType>` — What to create. Defaults to `custom` (CSV upload).
    
</dd>
</dl>

<dl>
<dd>

**autoRefresh:** `Optional<Boolean>` — Filter audiences only, and set only at creation. `true` (the default) rebuilds membership from the filters twice a day. `false` keeps whoever matched at creation and never rebuilds.
    
</dd>
</dl>

<dl>
<dd>

**columnMapping:** `Optional<CreateAudiencesRequestColumnMapping>` — Custom audiences only. Maps supported identity fields to CSV column headers. Map at least one of `email` or `phone`.
    
</dd>
</dl>

<dl>
<dd>

**count:** `Optional<Integer>` — Lookalikes only. Number of lookalike audiences to create (1–6).
    
</dd>
</dl>

<dl>
<dd>

**fileId:** `Optional<String>` — Custom audiences only. The uploaded customer CSV — a file id (`file_...`) returned by `POST /files`.
    
</dd>
</dl>

<dl>
<dd>

**filters:** `Optional<Map<String, Object>>` — Filter audiences only. The People filters that define membership, keyed exactly as `GET /people` accepts them — for example `{"os": "iOS", "country": "US"}`. Date filters must be rolling windows — `first_seen_within_days` or `last_seen_within_days` — so the audience re-anchors on every refresh; fixed dates such as `first_seen_after` are rejected. Source values are canonical source paths (`whop:<campaign>:<group>:<ad>`, `ext:<platform>:...`, `referrer:<domain>`, `direct`), exact or with a trailing `:*` wildcard.
    
</dd>
</dl>

<dl>
<dd>

**name:** `Optional<String>` — Audience display name. Required for custom audiences; lookalike names are generated from the source audience.
    
</dd>
</dl>

<dl>
<dd>

**percentage:** `Optional<Integer>` — Lookalikes only. Total similarity reach as a whole percent (1–20), sliced evenly across `count` — must be divisible by `count`.
    
</dd>
</dl>

<dl>
<dd>

**sourceAudienceId:** `Optional<String>` — Lookalikes only. The ready custom audience (`adaud_`) to build from; it needs at least 100 matched people.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.audiences.delete(id) -> DeleteAudiencesResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Deletes an audience so it is no longer available for targeting.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.audiences().delete(
    "id",
    DeleteAudiencesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Audience ID, prefixed `adaud_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.audiences.update(id, request) -> Audience</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Renames an audience. For an audience built from People filters that keeps itself up to date, pass `filters` to replace them, which rebuilds membership immediately. Whether an audience auto refreshes is set when it is created.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.audiences().update(
    "id",
    UpdateAudiencesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Audience ID, prefixed `adaud_`.
    
</dd>
</dl>

<dl>
<dd>

**filters:** `Optional<Map<String, Object>>` — Replaces the People filters that define membership. The whole definition is replaced rather than merged, so send every filter you want to keep — a filter you leave out stops applying. Keys and values are the ones `GET /people` accepts, such as an `os` of `iOS` or a `country` of `US`, and at least one filter is required. Date filters must be rolling windows — `first_seen_within_days` or `last_seen_within_days` — so the audience re-anchors every time it rebuilds; fixed dates such as `first_seen_after` are rejected, as is `audience_id`. An array value holds at most 500 items, and each value at most 10 KB. Only an audience with a `source_type` of `people_filter` and `auto_refresh` of `true` accepts filters: an uploaded list has no filters to replace, and with auto refresh off the audience keeps the people it matched when it was built, so create a new audience instead.
    
</dd>
</dl>

<dl>
<dd>

**name:** `Optional<String>` — New audience display name. A blank value is ignored rather than clearing the name.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.audiences.addPeople(id, request) -> Audience</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Adds users from a new CSV file to an existing uploaded custom audience. The file uses the audience's saved column mapping, processing happens in the background, and existing audience members remain unchanged.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.audiences().addPeople(
    "id",
    AddPeopleAudiencesRequest
        .builder()
        .fileId("file_xxxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Audience ID, prefixed `adaud_`.
    
</dd>
</dl>

<dl>
<dd>

**fileId:** `String` — The new customer CSV — a file id (`file_...`) returned by `POST /files`. Its headers must match the audience's saved column mapping.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## AuthorizedUsers
<details><summary><code>client.authorizedUsers.list() -> SyncPagingIterable&amp;lt;AuthorizedUserListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of authorized team members for a company, with optional filtering by user, role, and creation date.

Required permissions:
 - `company:authorized_user:read`
 - `member:email:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.authorizedUsers().list(
    ListAuthorizedUsersRequest
        .builder()
        .first(42)
        .last(42)
        .companyId("biz_xxxxxxxxxxxxxx")
        .userId("user_xxxxxxxxxxxxx")
        .createdBefore(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .createdAfter(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `Optional<String>` — The unique identifier of the company to list authorized users for.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — Filter results to a specific user to check if they are an authorized team member.
    
</dd>
</dl>

<dl>
<dd>

**role:** `Optional<AuthorizedUserRoles>` 
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<OffsetDateTime>` — Only return authorized users created before this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<OffsetDateTime>` — Only return authorized users created after this timestamp.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.authorizedUsers.create(request) -> AuthorizedUser</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Add a new authorized user to a company.

Required permissions:
 - `authorized_user:create`
 - `member:email:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.authorizedUsers().create(
    CreateAuthorizedUsersRequest
        .builder()
        .companyId("biz_xxxxxxxxxxxxxx")
        .role(GrantableAuthorizedUserRoles.OWNER)
        .userId("user_xxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**companyId:** `String` — The ID of the company to add the authorized user to.
    
</dd>
</dl>

<dl>
<dd>

**elevation:** `Optional<CreateAuthorizedUsersRequestElevation>` — Re-authentication proof required to perform this sensitive action.
    
</dd>
</dl>

<dl>
<dd>

**role:** `GrantableAuthorizedUserRoles` — The role to assign to the authorized user within the company. Supported roles: 'moderator', 'sales_manager'.
    
</dd>
</dl>

<dl>
<dd>

**sendEmails:** `Optional<Boolean>` — Whether to send notification emails to the user on creation.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `String` — The ID of the user to add as an authorized user.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.authorizedUsers.retrieve(id) -> AuthorizedUser</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing authorized user.

Required permissions:
 - `company:authorized_user:read`
 - `member:email:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.authorizedUsers().retrieve(
    "ausr_xxxxxxxxxxxxx",
    RetrieveAuthorizedUsersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the authorized user to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.authorizedUsers.delete(id) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Remove an authorized user from a company.

Required permissions:
 - `authorized_user:delete`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.authorizedUsers().delete(
    "ausr_xxxxxxxxxxxxx",
    DeleteAuthorizedUsersRequest
        .builder()
        .companyId("biz_xxxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ID of the authorized user or user to remove.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `Optional<String>` — The ID of the company the authorized user belongs to. Optional if the authorized user ID is provided.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Bounties
<details><summary><code>client.bounties.list() -> SyncPagingIterable&amp;lt;BountyListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists bounties visible to the credential — for an account API key, the account's bounties including scheduled drafts; for a user token, the bounties the user can see and work.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.bounties().list(
    ListBountiesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — Scope the list to this account (`biz_` tag). Requires read access to the account; account API keys may pass their own account or a connected account.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — List the bounties this user participated in (`user_` tag). Must be the authenticated user.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<ListBountiesRequestStatus>` — Filter by lifecycle state.
    
</dd>
</dl>

<dl>
<dd>

**businessGoalType:** `Optional<ListBountiesRequestBusinessGoalType>` — Filter by the poster's declared goal. Bounties created before the goal taxonomy carry no goal and never match this filter.
    
</dd>
</dl>

<dl>
<dd>

**country:** `Optional<String>` — Only bounties workable from this country, as an ISO 3166-1 alpha-2 code. Bounties with no country targeting are workable worldwide and always match.
    
</dd>
</dl>

<dl>
<dd>

**experienceId:** `Optional<String>` — Only bounties posted to this forum experience, prefixed `exp_`. An unknown experience, or one outside the caller's scope, matches nothing.
    
</dd>
</dl>

<dl>
<dd>

**query:** `Optional<String>` — Substring match on the bounty title or ID.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Only bounties created after this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Only bounties created before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListBountiesRequestOrder>` — Sort field.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListBountiesRequestDirection>` — Sort direction.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Number of bounties to return from the start of the window.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor to paginate forwards from.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Number of bounties to return from the end of the window.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Cursor to paginate backwards from.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.bounties.create(request) -> Bounty</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Creates a bounty and escrows its reward pool. Publishes immediately, or as a scheduled draft when you set `publish_at`.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.bounties().create(
    CreateBountiesRequest
        .builder()
        .description("Record one continuous pass of a full interior detail, dash to trunk, on a customer vehicle.")
        .grossRewardAmount(40.0)
        .title("Record interior detailing passes")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**acceptedSubmissionsLimit:** `Optional<Integer>` — Number of submissions that can be accepted (winner slots). Defaults to 1. The escrowed total is `gross_reward_amount` times this limit and must be at least $5.
    
</dd>
</dl>

<dl>
<dd>

**acceptedSubmissionsPerUserLimit:** `Optional<Integer>` — How many winner slots one worker can win. Defaults to `1`. Wins plus proofs awaiting review never exceed this number, and a worker runs one attempt at a time. Cannot exceed `accepted_submissions_limit`.
    
</dd>
</dl>

<dl>
<dd>

**accountId:** `Optional<String>` — Account whose balance funds the bounty pool (`biz_` tag). Defaults to the caller's personal balance. Requires permission to move the account's funds.
    
</dd>
</dl>

<dl>
<dd>

**allowedCountryCodes:** `Optional<List<String>>` — Countries whose residents can work the bounty, as ISO 3166 alpha-2 codes. Empty means worldwide.
    
</dd>
</dl>

<dl>
<dd>

**businessGoalType:** `Optional<CreateBountiesRequestBusinessGoalType>` — What the poster wants the work to achieve, declared once here.
    
</dd>
</dl>

<dl>
<dd>

**captureSpec:** `Optional<CreateBountiesRequestCaptureSpec>` — Per-bounty overrides of the served capture contract. Only accepted when `business_goal_type` is `data_capture`; omitted fields keep the platform defaults, and the resulting contract is echoed back as `capture_spec` on the bounty.
    
</dd>
</dl>

<dl>
<dd>

**description:** `String` — Full task instructions shown to workers.
    
</dd>
</dl>

<dl>
<dd>

**experienceId:** `Optional<String>` — Experience to host the bounty in (`exp_` tag). Any visibility — public for an open bounty, private for an invited one. Required unless account_id is set, in which case the bounty anchors in that account's public forum.
    
</dd>
</dl>

<dl>
<dd>

**frequency:** `Optional<CreateBountiesRequestFrequency>` — How often the schedule creates a new bounty. Each occurrence is a separate bounty. Defaults to `once`; only applies with `publish_at`.
    
</dd>
</dl>

<dl>
<dd>

**grossRewardAmount:** `Double` — Gross bounty-pool amount (USD) escrowed per accepted submission, in whole dollars. Platform fees and affiliate shares are paid from this amount.
    
</dd>
</dl>

<dl>
<dd>

**publishAt:** `Optional<String>` — ISO 8601 time to publish the bounty. When set, the bounty is created as a hidden draft and funded + published at this time instead of immediately.
    
</dd>
</dl>

<dl>
<dd>

**publishAtTimezone:** `Optional<String>` — IANA timezone for recurring occurrences. Required when publish_at is set.
    
</dd>
</dl>

<dl>
<dd>

**title:** `String` — Short name of the task shown to workers.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.bounties.retrieve(id) -> Bounty</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a bounty by ID. Authentication is optional: a request with no credential reads the bounty when it is publicly visible — published or completed, and not restricted to a private experience's members. Bounties outside the caller's scope, and bounties not publicly visible to an anonymous caller, return `404`.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.bounties().retrieve(
    "id",
    RetrieveBountiesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Bounty ID (`bnty_` tag).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.bounties.update(id, request) -> Bounty</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Updates a bounty. A published bounty accepts title, description, and country targeting while it is still open with nothing under review. A scheduled (not-yet-published) draft additionally accepts the reward, winner slots, and schedule.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.bounties().update(
    "id",
    UpdateBountiesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Bounty ID (`bnty_` tag).
    
</dd>
</dl>

<dl>
<dd>

**acceptedSubmissionsLimit:** `Optional<Integer>` — Scheduled drafts only. Number of submissions that can be accepted (winner slots).
    
</dd>
</dl>

<dl>
<dd>

**acceptedSubmissionsPerUserLimit:** `Optional<Integer>` — How many winner slots one worker can win. Defaults to `1`. Wins plus proofs awaiting review never exceed this number, and a worker runs one attempt at a time. Cannot exceed `accepted_submissions_limit`. Editable while the bounty is still open with nothing under review.
    
</dd>
</dl>

<dl>
<dd>

**allowedCountryCodes:** `Optional<List<String>>` — Replace the countries whose residents can work the bounty, as ISO 3166 alpha-2 codes. Empty means worldwide.
    
</dd>
</dl>

<dl>
<dd>

**businessGoalType:** `Optional<UpdateBountiesRequestBusinessGoalType>` — What the poster wants the work to achieve, declared once here.
    
</dd>
</dl>

<dl>
<dd>

**description:** `Optional<String>` — New full task instructions.
    
</dd>
</dl>

<dl>
<dd>

**frequency:** `Optional<UpdateBountiesRequestFrequency>` — Scheduled drafts only. How often the schedule creates a new bounty.
    
</dd>
</dl>

<dl>
<dd>

**grossRewardAmount:** `Optional<Double>` — Scheduled drafts only. Gross bounty-pool amount (USD) escrowed per accepted submission. The escrowed total (this times accepted_submissions_limit) must stay at least $5.
    
</dd>
</dl>

<dl>
<dd>

**publishAt:** `Optional<String>` — Scheduled drafts only. New ISO 8601 time to publish the draft. Must be in the future.
    
</dd>
</dl>

<dl>
<dd>

**publishAtTimezone:** `Optional<String>` — Scheduled drafts only. IANA timezone for recurring occurrences.
    
</dd>
</dl>

<dl>
<dd>

**title:** `Optional<String>` — New short name of the task.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.bounties.cancel(id) -> Bounty</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Cancels a bounty. With no in-flight work, it cancels immediately and refunds the funder. Otherwise it stops new submissions and cancels once the in-flight work resolves and pays out. Repeating the request is a no-op. A bounty that already paid out every slot returns `400`.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.bounties().cancel(
    "id",
    CancelBountiesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Bounty ID (`bnty_` tag).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Bounty Submissions
<details><summary><code>client.bountySubmissions.list() -> SyncPagingIterable&amp;lt;BountySubmission&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists bounty submissions visible to the credential — for a user token, the submissions they authored plus those on bounties they posted; for an account API key, the submissions on the account's bounties. For the anonymous view of one bounty's reviewed work, use the submissions list under the bounty instead.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.bountySubmissions().list(
    ListBountySubmissionsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — Scope the list to submissions on this account's bounties (`biz_` tag). Requires read access to the account.
    
</dd>
</dl>

<dl>
<dd>

**bountyId:** `Optional<String>` — Only submissions on this bounty (`bnty_` tag).
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<ListBountySubmissionsRequestStatus>` — Filter by lifecycle state.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Only submissions created after this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Only submissions created before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListBountySubmissionsRequestOrder>` — Sort field.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListBountySubmissionsRequestDirection>` — Sort direction.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Number of submissions to return from the start of the window.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor to paginate forwards from.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Number of submissions to return from the end of the window.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Cursor to paginate backwards from.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.bountySubmissions.create(request) -> BountySubmission</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Creates a submission on a workforce bounty. Include a `deliverable` payload — any combination of links and uploaded files, with at least one of the two — and the submission goes straight to review; create is the only step. For `data_capture` bounties, omit the deliverable: this starts a claimed attempt whose proof accumulates server-side, and the separate submit endpoint sends it to review once complete. Requires a user credential — account API keys cannot author submissions.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.bountySubmissions().create(
    CreateBountySubmissionsRequest
        .builder()
        .bountyId("bnty_xxxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**affiliateCode:** `Optional<String>` — Affiliate code crediting the referrer, when the worker arrived through one.
    
</dd>
</dl>

<dl>
<dd>

**bountyId:** `String` — The bounty to submit to (`bnty_` tag).
    
</dd>
</dl>

<dl>
<dd>

**deliverable:** `Optional<CreateBountySubmissionsRequestDeliverable>` — The submitted work. Combine `urls`, `file_ids`, and `caption` freely; at least one link or file is required.
    
</dd>
</dl>

<dl>
<dd>

**metadata:** `Optional<CreateBountySubmissionsRequestMetadata>` — Optional capture metadata describing where and how the footage was recorded. Persisted on the submission. On a `data_capture` bounty every field except `fov` is required whenever metadata is provided.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.bountySubmissions.retrieve(id) -> BountySubmission</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves one bounty submission the credential can see — one the caller authored, or one on a bounty they posted or their account owns. Reading another member's work on an account's bounty takes `account_id`, the same way the list does.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.bountySubmissions().retrieve(
    "id",
    RetrieveBountySubmissionsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The bounty submission to act on (`btys_` tag).
    
</dd>
</dl>

<dl>
<dd>

**accountId:** `Optional<String>` — Read the submission as this account (`biz_` tag), scoping the lookup to its bounties rather than the caller's own work. Requires read access to the account. Without it the lookup covers only what the credential owns — the submissions the caller authored plus those on bounties they posted.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.bountySubmissions.delete(id) -> DeleteBountySubmissionsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Cancels the caller's own active attempt on a bounty and discards any accumulated capture clips. Only the worker who started the attempt can cancel it — account API keys cannot.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.bountySubmissions().delete(
    "id",
    DeleteBountySubmissionsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The bounty submission to act on (`btys_` tag).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.bountySubmissions.submit(id, request) -> BountySubmission</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Submits a claimed attempt for review. A livestream attempt needs an ended proof stream and can attach an optional `deliverable` — links, files, and a caption in any combination; if the attempt already went to review when its stream ended, the payload attaches to it once, until reviewers start voting. A data capture attempt instead needs enough validated clip time and takes no payload. Only the worker who started the attempt can submit it — account API keys cannot.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.bountySubmissions().submit(
    "id",
    SubmitBountySubmissionsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The claimed attempt to submit for review (`btys_` tag).
    
</dd>
</dl>

<dl>
<dd>

**deliverable:** `Optional<SubmitBountySubmissionsRequestDeliverable>` — Work to attach to the submission. Combine `urls`, `file_ids`, and `caption` freely; all are optional.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## CardTransactions
<details><summary><code>client.cardTransactions.list() -> SyncPagingIterable&amp;lt;CardTransaction&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists an account's card transactions, newest first. Defaults to the account the credential belongs to. Covers every card the owner has ever had, including canceled cards and spend that predates a re-application, and team members only see transactions on the cards assigned to them. Pass `transaction_ids` to fetch specific transactions instead of paging for them.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.cardTransactions().list(
    ListCardTransactionsRequest
        .builder()
        .transactionIds(
            Arrays.asList("citx_xxxxxxxxxxxxxx")
        )
        .cardId(
            Arrays.asList("icrd_xxxxxxxxxxxxxx")
        )
        .cardholderId(
            Arrays.asList("user_xxxxxxxxxxxxxx")
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The account whose card transactions to list, prefixed `biz_`. Defaults to the credential's account.
    
</dd>
</dl>

<dl>
<dd>

**transactionIds:** `Optional<String>` — Return only these card transactions, each prefixed `citx_`. Repeat the parameter, or pass one comma-separated value.
    
</dd>
</dl>

<dl>
<dd>

**cardId:** `Optional<String>` — Return only transactions charged to these cards, each prefixed `icrd_`.
    
</dd>
</dl>

<dl>
<dd>

**cardholderId:** `Optional<String>` — Return only transactions on cards assigned to these users, each prefixed `user_`.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<ListCardTransactionsRequestStatus>` — Return only transactions with this status.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Return only transactions authorized at or after this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Return only transactions authorized at or before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListCardTransactionsRequestOrder>` — The field to sort by. Defaults to `created_at`.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListCardTransactionsRequestDirection>` — The sort direction. Defaults to `desc`.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of card transactions to return.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor; returns card transactions after this position.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — The number of card transactions to return, counting back from the end.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — A cursor; returns card transactions before this position.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.cardTransactions.retrieve(id) -> CardTransaction</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Fetches a single card transaction by its `citx_` identifier. The owner defaults to the account the credential belongs to.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.cardTransactions().retrieve(
    "id",
    RetrieveCardTransactionsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The card transaction ID, prefixed `citx_`.
    
</dd>
</dl>

<dl>
<dd>

**accountId:** `Optional<String>` — The account that owns the transaction, prefixed `biz_`. Defaults to the credential's account.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Cards
<details><summary><code>client.cards.list() -> ListCardsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the Whop cards of an account or user, including ones still being set up. Team members only see the cards assigned to them.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.cards().list(
    ListCardsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The owning account ID (a biz_ identifier). Provide this or user_id.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — The owning user ID (a user_ identifier). Provide this or account_id.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.cards.create(request) -> CreateCardsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Issue a virtual card, or apply for card issuing.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.cards().create(
    CreateCardsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The owning account ID (a biz_ identifier). Provide this or user_id.
    
</dd>
</dl>

<dl>
<dd>

**assignedUserId:** `Optional<String>` — The account member (a user_ identifier) to assign the card to. Required for business card issuing accounts.
    
</dd>
</dl>

<dl>
<dd>

**name:** `Optional<String>` — A display name for the card.
    
</dd>
</dl>

<dl>
<dd>

**spendLimit:** `Optional<Double>` — Spending limit amount, in dollars.
    
</dd>
</dl>

<dl>
<dd>

**spendLimitFrequency:** `Optional<CreateCardsRequestSpendLimitFrequency>` — The window the spend limit applies to.
    
</dd>
</dl>

<dl>
<dd>

**transactionLimit:** `Optional<Double>` — Per-transaction limit amount, in dollars.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — The owning user ID (a user_ identifier). Provide this or account_id.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.cards.retrieve(id) -> RetrieveCardsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieve a single card.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.cards().retrieve(
    "id",
    RetrieveCardsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Card ID to retrieve, prefixed `icrd_`.
    
</dd>
</dl>

<dl>
<dd>

**accountId:** `Optional<String>` — The owning account ID (a biz_ identifier). Provide this or user_id.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — The owning user ID (a user_ identifier). Provide this or account_id.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.cards.update(id, request) -> UpdateCardsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Update, freeze, or cancel a card. Updating the card's name, billing address, or limits requires both `payout:account:update` and `company:balance:read`; a card's assigned holder may update their own card's pin and frozen state with any user token.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.cards().update(
    "id",
    UpdateCardsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Card ID to retrieve, prefixed `icrd_`.
    
</dd>
</dl>

<dl>
<dd>

**accountId:** `Optional<String>` — The owning account ID (a biz_ identifier). Provide this or user_id.
    
</dd>
</dl>

<dl>
<dd>

**billing:** `Optional<UpdateCardsRequestBilling>` — New billing address. Requires line1, city, region, postal_code, and country_code. On an invited card, passing billing alone (as the invited user) completes onboarding and starts card provisioning.
    
</dd>
</dl>

<dl>
<dd>

**canceled:** `Optional<Boolean>` — Pass `true` to permanently cancel the card. A canceled card cannot be uncanceled. Cannot be combined with other fields.
    
</dd>
</dl>

<dl>
<dd>

**frozen:** `Optional<Boolean>` — Pass `true` to freeze the card, `false` to unfreeze it. The assigned cardholder may freeze their own card without the payout:account:update scope.
    
</dd>
</dl>

<dl>
<dd>

**name:** `Optional<String>` — A display name for the card.
    
</dd>
</dl>

<dl>
<dd>

**pin:** `Optional<String>` — New 4-digit PIN. Can only be set on a card assigned to the acting user, who may set it without the payout:account:update scope.
    
</dd>
</dl>

<dl>
<dd>

**removeLimit:** `Optional<Boolean>` — Pass `true` to remove the spending limit (make the card unlimited).
    
</dd>
</dl>

<dl>
<dd>

**spendLimit:** `Optional<Double>` — Spending limit amount, in dollars.
    
</dd>
</dl>

<dl>
<dd>

**spendLimitFrequency:** `Optional<UpdateCardsRequestSpendLimitFrequency>` — The window the spend limit applies to.
    
</dd>
</dl>

<dl>
<dd>

**transactionLimit:** `Optional<Double>` — Per-transaction limit amount, in dollars.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — The owning user ID (a user_ identifier). Provide this or account_id.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## ChatChannels
<details><summary><code>client.chatChannels.list() -> SyncPagingIterable&amp;lt;ChatChannelListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of chat channels within a specific company, with optional filtering by product.

Required permissions:
 - `chat:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.chatChannels().list(
    ListChatChannelsRequest
        .builder()
        .companyId("biz_xxxxxxxxxxxxxx")
        .first(42)
        .last(42)
        .productId("prod_xxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `String` — The unique identifier of the company to list chat channels for.
    
</dd>
</dl>

<dl>
<dd>

**productId:** `Optional<String>` — The unique identifier of a product to filter by. When set, only chat channels connected to this product are returned.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.chatChannels.retrieve(id) -> ChatChannel</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing chat channel.

Required permissions:
 - `chat:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.chatChannels().retrieve(
    "id",
    RetrieveChatChannelsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the chat channel or experience to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.chatChannels.update(id, request) -> ChatChannel</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Update moderation settings for a chat channel, such as who can post, banned words, and media restrictions.

Required permissions:
 - `chat:moderate`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.chatChannels().update(
    "id",
    UpdateChatChannelsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the chat channel to update. Accepts either an experience ID (e.g. 'exp_xxxxx') or a chat channel ID.
    
</dd>
</dl>

<dl>
<dd>

**banMedia:** `Optional<Boolean>` — Whether media uploads such as images and videos are banned in this chat channel.
    
</dd>
</dl>

<dl>
<dd>

**banUrls:** `Optional<Boolean>` — Whether URLs and links are banned from being posted in this chat channel.
    
</dd>
</dl>

<dl>
<dd>

**bannedWords:** `Optional<List<String>>` — A list of words that are automatically blocked from messages in this chat channel. For example, ['spam', 'scam'].
    
</dd>
</dl>

<dl>
<dd>

**userPostsCooldownSeconds:** `Optional<Integer>` — The minimum number of seconds a user must wait between sending messages in this chat channel.
    
</dd>
</dl>

<dl>
<dd>

**whoCanPost:** `Optional<WhoCanPostTypes>` — Controls which roles are allowed to send messages in this chat channel.
    
</dd>
</dl>

<dl>
<dd>

**whoCanReact:** `Optional<WhoCanReactTypes>` — Controls which roles are allowed to add reactions to messages in this chat channel.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Checkout Configurations
<details><summary><code>client.checkoutConfigurations.list() -> SyncPagingIterable&amp;lt;ListCheckoutConfigurationsResponseDataItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists checkout configurations for an account.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.checkoutConfigurations().list(
    ListCheckoutConfigurationsRequest
        .builder()
        .accountId("account_id")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `String` — Account ID, prefixed `biz_`.
    
</dd>
</dl>

<dl>
<dd>

**planId:** `Optional<String>` — Only return checkout configurations for this plan ID, prefixed `plan_`.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Only return checkout configurations created before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Only return checkout configurations created after this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListCheckoutConfigurationsRequestOrder>` — Field used to sort checkout configurations.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListCheckoutConfigurationsRequestDirection>` — Sort direction. Defaults to `desc`.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Number of checkout configurations to return.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor for the next page of results.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.checkoutConfigurations.create(request) -> CreateCheckoutConfigurationsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Creates a reusable checkout configuration for an existing or inline plan.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.checkoutConfigurations().create(
    CreateCheckoutConfigurationsRequest
        .builder()
        .accountId("biz_xxxxxxxxxxxxxx")
        .planId("plan_xxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — Account ID, prefixed `biz_`.
    
</dd>
</dl>

<dl>
<dd>

**affiliateCode:** `Optional<String>` — Affiliate code to apply to the checkout.
    
</dd>
</dl>

<dl>
<dd>

**currency:** `Optional<String>` — Currency used for setup-mode payment method availability.
    
</dd>
</dl>

<dl>
<dd>

**metadata:** `Optional<Map<String, Object>>` — Custom key-value metadata copied to payments and memberships.
    
</dd>
</dl>

<dl>
<dd>

**mode:** `Optional<CreateCheckoutConfigurationsRequestMode>` — Controls whether checkout charges the buyer immediately or saves payment details for later. Defaults to `payment`.
    
</dd>
</dl>

<dl>
<dd>

**paymentMethodConfiguration:** `Optional<CreateCheckoutConfigurationsRequestPaymentMethodConfiguration>` — Payment method overrides for this checkout. `null` uses the plan or platform defaults.
    
</dd>
</dl>

<dl>
<dd>

**plan:** `Optional<CreateCheckoutConfigurationsRequestPlan>` — Plan attributes used to create or find a plan for this checkout configuration. Mutually exclusive with `plan_id`.
    
</dd>
</dl>

<dl>
<dd>

**planId:** `Optional<String>` — Existing plan ID, prefixed `plan_`. Mutually exclusive with `plan`.
    
</dd>
</dl>

<dl>
<dd>

**redirectUrl:** `Optional<String>` — URL customers are sent to after checkout.
    
</dd>
</dl>

<dl>
<dd>

**threeDsLevel:** `Optional<CreateCheckoutConfigurationsRequestThreeDsLevel>` — 3D Secure behavior for this checkout.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.checkoutConfigurations.retrieve(id) -> RetrieveCheckoutConfigurationsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a checkout configuration by ID. This endpoint is public so a checkout page can load from the configuration URL.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.checkoutConfigurations().retrieve(
    "id",
    RetrieveCheckoutConfigurationsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ID of the checkout configuration.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.checkoutConfigurations.delete(id) -> DeleteCheckoutConfigurationsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Deletes a checkout configuration so its checkout URL can no longer be used.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.checkoutConfigurations().delete(
    "id",
    DeleteCheckoutConfigurationsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ID of the checkout configuration.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Companies
<details><summary><code>client.companies.list() -> SyncPagingIterable&amp;lt;CompanyListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of companies. When parent_company_id is provided, lists connected accounts under that platform. When omitted, lists companies the current user has access to.

Required permissions:
 - `company:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.companies().list(
    ListCompaniesRequest
        .builder()
        .first(42)
        .last(42)
        .createdBefore(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .createdAfter(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**parentCompanyId:** `Optional<String>` — The unique identifier of the parent platform company. When provided, lists connected accounts under that platform. Omit to list the current user's own companies.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<Direction>` 
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<OffsetDateTime>` — Only return companies created before this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<OffsetDateTime>` — Only return companies created after this timestamp.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.companies.create(request) -> Company</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Create a new company. Pass parent_company_id to create a connected account under a platform, or omit it to create a company for the current user.

Required permissions:
 - `company:create`
 - `company:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.companies().create(
    CreateCompaniesRequest
        .builder()
        .title("title")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**country:** `Optional<Countries>` — The country the company is located in. Defaults to the parent company's country for connected accounts, or the owner's IP-derived country.
    
</dd>
</dl>

<dl>
<dd>

**description:** `Optional<String>` — A promotional pitch displayed to potential customers on the company's store page.
    
</dd>
</dl>

<dl>
<dd>

**email:** `Optional<String>` — The email address of the user who will own the connected account. Required when parent_company_id is provided.
    
</dd>
</dl>

<dl>
<dd>

**logo:** `Optional<CreateCompaniesRequestLogo>` — The company's logo image. Accepts PNG, JPEG, or GIF format.
    
</dd>
</dl>

<dl>
<dd>

**metadata:** `Optional<Map<String, Object>>` — A key-value JSON object of custom metadata to store on the company.
    
</dd>
</dl>

<dl>
<dd>

**parentCompanyId:** `Optional<String>` — The unique identifier of the parent platform company. When provided, creates a connected account under that platform. Omit to create a company for the current user.
    
</dd>
</dl>

<dl>
<dd>

**sendCustomerEmails:** `Optional<Boolean>` — Whether Whop sends transactional emails to customers on behalf of this company. Only applies when creating a connected account.
    
</dd>
</dl>

<dl>
<dd>

**title:** `String` — The display name of the company shown to customers.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.companies.retrieve(id) -> Company</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing company.

Required permissions:
 - `company:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.companies().retrieve(
    "biz_xxxxxxxxxxxxxx",
    RetrieveCompaniesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier or route slug of the company.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.companies.update(id, request) -> Company</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Update a company's title, description, logo, and other settings.

Required permissions:
 - `company:update`
 - `company:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.companies().update(
    "biz_xxxxxxxxxxxxxx",
    UpdateCompaniesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the company to update.
    
</dd>
</dl>

<dl>
<dd>

**affiliateApplicationRequired:** `Optional<Boolean>` — Whether prospective affiliates must submit an application before they can promote this company.
    
</dd>
</dl>

<dl>
<dd>

**affiliateInstructions:** `Optional<String>` — Guidelines and instructions shown to affiliates explaining how to promote this company's products.
    
</dd>
</dl>

<dl>
<dd>

**bannerImage:** `Optional<UpdateCompaniesRequestBannerImage>` — The company's banner image. Accepts PNG or JPEG format.
    
</dd>
</dl>

<dl>
<dd>

**description:** `Optional<String>` — A promotional pitch displayed to potential customers on the company's store page.
    
</dd>
</dl>

<dl>
<dd>

**featuredAffiliateProductId:** `Optional<String>` — The ID of the product to feature on this company's affiliate page. Pass null to clear.
    
</dd>
</dl>

<dl>
<dd>

**logo:** `Optional<UpdateCompaniesRequestLogo>` — The company's logo image. Accepts PNG, JPEG, or GIF format.
    
</dd>
</dl>

<dl>
<dd>

**route:** `Optional<String>` — The unique URL slug for the company's store page. Must be lowercase and can include hyphens (e.g., 'my-company'). If not provided, the route will remain unchanged.
    
</dd>
</dl>

<dl>
<dd>

**sendCustomerEmails:** `Optional<Boolean>` — Whether Whop sends transactional emails (receipts, renewals, cancelations) to customers on behalf of this company.
    
</dd>
</dl>

<dl>
<dd>

**socialLinks:** `Optional<List<UpdateCompaniesRequestSocialLinksItem>>` — The social media links to display on the company's store page. Pass the full list of desired social links — any existing links not included will be removed.
    
</dd>
</dl>

<dl>
<dd>

**targetAudience:** `Optional<String>` — The target audience for this company (e.g., 'beginner day traders aged 18-25 looking to learn options').
    
</dd>
</dl>

<dl>
<dd>

**title:** `Optional<String>` — The display name of the company shown to customers.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.companies.createApiKey(parentCompanyId, request) -> CreateApiKeyCompaniesResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Create an API key for a connected account (child company) owned by a parent company.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.companies().createApiKey(
    "parent_company_id",
    CreateApiKeyCompaniesRequest
        .builder()
        .childCompanyId("child_company_id")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**parentCompanyId:** `String` — The unique identifier of the parent platform company (e.g. 'biz_xxx').
    
</dd>
</dl>

<dl>
<dd>

**childCompanyId:** `String` — The unique identifier of the connected account to create the API key for (e.g. 'biz_xxx').
    
</dd>
</dl>

<dl>
<dd>

**name:** `Optional<String>` — A human-readable name for the API key, such as 'Production API Key'.
    
</dd>
</dl>

<dl>
<dd>

**permissions:** `Optional<List<CreateApiKeyCompaniesRequestPermissionsItem>>` — Granular permission statements defining which actions this API key can perform. Either permissions or role must be provided.
    
</dd>
</dl>

<dl>
<dd>

**role:** `Optional<PermissionSystemRoles>` — A system role to inherit permissions from (e.g. owner, admin, moderator). Either role or permissions must be provided.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## CompanyTokenTransactions
<details><summary><code>client.companyTokenTransactions.list() -> SyncPagingIterable&amp;lt;CompanyTokenTransactionListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of token transactions for a user or company, depending on the authenticated actor, with optional filtering by user and transaction type.

Required permissions:
 - `company_token_transaction:read`
 - `member:basic:read`
 - `company:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.companyTokenTransactions().list(
    ListCompanyTokenTransactionsRequest
        .builder()
        .companyId("biz_xxxxxxxxxxxxxx")
        .first(42)
        .last(42)
        .userId("user_xxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `String` — The unique identifier of the company to list token transactions for.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — Filter transactions to only those involving this specific user.
    
</dd>
</dl>

<dl>
<dd>

**transactionType:** `Optional<CompanyTokenTransactionTypes>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.companyTokenTransactions.create(request) -> CompanyTokenTransaction</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Create a token transaction to add, subtract, or transfer tokens for a member within a company.

Required permissions:
 - `company_token_transaction:create`
 - `member:basic:read`
 - `company:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.companyTokenTransactions().create(
    CreateCompanyTokenTransactionsRequest.transfer(
        CreateCompanyTokenTransactionsRequestTransfer
            .builder()
            .amount(6.9)
            .companyId("biz_xxxxxxxxxxxxxx")
            .destinationUserId("destination_user_id")
            .userId("user_xxxxxxxxxxxxx")
            .build()
    )
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**request:** `CreateCompanyTokenTransactionsRequest` — Parameters for CreateCompanyTokenTransaction
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.companyTokenTransactions.retrieve(id) -> CompanyTokenTransaction</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing company token transaction.

Required permissions:
 - `company_token_transaction:read`
 - `member:basic:read`
 - `company:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.companyTokenTransactions().retrieve(
    "id",
    RetrieveCompanyTokenTransactionsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the token transaction to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## CourseChapters
<details><summary><code>client.courseChapters.list() -> SyncPagingIterable&amp;lt;CourseChapterListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of chapters within a course, ordered by position.

Required permissions:
 - `courses:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courseChapters().list(
    ListCourseChaptersRequest
        .builder()
        .courseId("cors_xxxxxxxxxxxxx")
        .first(42)
        .last(42)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**courseId:** `String` — The unique identifier of the course to list chapters for.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.courseChapters.create(request) -> CourseChapter</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Create a new chapter within a course to organize lessons into sections.

Required permissions:
 - `courses:update`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courseChapters().create(
    CreateCourseChaptersRequest
        .builder()
        .courseId("cors_xxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**courseId:** `String` — The unique identifier of the course to create the chapter in (e.g., "course_XXXXX").
    
</dd>
</dl>

<dl>
<dd>

**title:** `Optional<String>` — The display title of the chapter (e.g., "Module 1: Introduction").
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.courseChapters.retrieve(id) -> CourseChapter</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing course chapter.

Required permissions:
 - `courses:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courseChapters().retrieve(
    "chap_xxxxxxxxxxxxx",
    RetrieveCourseChaptersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the chapter to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.courseChapters.delete(id) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Permanently delete a chapter and all of its lessons from a course.

Required permissions:
 - `courses:update`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courseChapters().delete(
    "chap_xxxxxxxxxxxxx",
    DeleteCourseChaptersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the chapter to delete (e.g., "chap_XXXXX").
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.courseChapters.update(id, request) -> CourseChapter</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Update a chapter's title within a course.

Required permissions:
 - `courses:update`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courseChapters().update(
    "chap_xxxxxxxxxxxxx",
    UpdateCourseChaptersRequest
        .builder()
        .title("title")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the chapter to update (e.g., "chap_XXXXX").
    
</dd>
</dl>

<dl>
<dd>

**title:** `String` — The new display title of the chapter (e.g., "Module 1: Introduction").
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## CourseLessonInteractions
<details><summary><code>client.courseLessonInteractions.list() -> SyncPagingIterable&amp;lt;CourseLessonInteractionListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of lesson interactions, filtered by lesson, course, user, or completion status.

Required permissions:
 - `courses:read`
 - `course_analytics:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courseLessonInteractions().list(
    ListCourseLessonInteractionsRequest
        .builder()
        .first(42)
        .last(42)
        .userId("user_xxxxxxxxxxxxx")
        .lessonId("lesn_xxxxxxxxxxxxx")
        .courseId("cors_xxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — The unique identifier of the user to filter lesson interactions for.
    
</dd>
</dl>

<dl>
<dd>

**lessonId:** `Optional<String>` — The unique identifier of the lesson to filter interactions for.
    
</dd>
</dl>

<dl>
<dd>

**courseId:** `Optional<String>` — The unique identifier of the course to filter interactions for.
    
</dd>
</dl>

<dl>
<dd>

**completed:** `Optional<Boolean>` — Whether to filter for completed or in-progress lesson interactions.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.courseLessonInteractions.retrieve(id) -> CourseLessonInteraction</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing course lesson interaction.

Required permissions:
 - `courses:read`
 - `course_analytics:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courseLessonInteractions().retrieve(
    "crsli_xxxxxxxxxxxx",
    RetrieveCourseLessonInteractionsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the lesson interaction to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## CourseLessons
<details><summary><code>client.courseLessons.list() -> SyncPagingIterable&amp;lt;CourseLessonListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of lessons within a course or chapter, ordered by position.

Required permissions:
 - `courses:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courseLessons().list(
    ListCourseLessonsRequest
        .builder()
        .first(42)
        .last(42)
        .courseId("cors_xxxxxxxxxxxxx")
        .chapterId("chap_xxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**courseId:** `Optional<String>` — The unique identifier of the course to return all lessons across all chapters.
    
</dd>
</dl>

<dl>
<dd>

**chapterId:** `Optional<String>` — The unique identifier of a chapter to return only its lessons.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.courseLessons.create(request) -> CourseLesson</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Create a new lesson within a course chapter. Lessons can contain video, text, or assessment content.

Required permissions:
 - `courses:update`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courseLessons().create(
    CreateCourseLessonsRequest
        .builder()
        .chapterId("chap_xxxxxxxxxxxxx")
        .lessonType(LessonTypes.TEXT)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**chapterId:** `String` — The unique identifier of the chapter to create the lesson in (e.g., "chap_XXXXX").
    
</dd>
</dl>

<dl>
<dd>

**content:** `Optional<String>` — The Markdown content body of the lesson.
    
</dd>
</dl>

<dl>
<dd>

**daysFromCourseStartUntilUnlock:** `Optional<Integer>` — The number of days after a student starts the course before this lesson becomes accessible.
    
</dd>
</dl>

<dl>
<dd>

**embedId:** `Optional<String>` — The external video identifier for embedded content (e.g., a YouTube video ID or Loom share ID).
    
</dd>
</dl>

<dl>
<dd>

**embedType:** `Optional<EmbedTypes>` — The type of video embed for this lesson, such as YouTube or Loom.
    
</dd>
</dl>

<dl>
<dd>

**lessonType:** `LessonTypes` — The content type of the lesson, such as video, text, quiz, or knowledge check.
    
</dd>
</dl>

<dl>
<dd>

**thumbnail:** `Optional<CreateCourseLessonsRequestThumbnail>` — The thumbnail image for the lesson in PNG, JPEG, or GIF format.
    
</dd>
</dl>

<dl>
<dd>

**title:** `Optional<String>` — The display title of the lesson (e.g., "Getting Started with APIs").
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.courseLessons.retrieve(id) -> CourseLesson</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing course lesson.

Required permissions:
 - `courses:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courseLessons().retrieve(
    "lesn_xxxxxxxxxxxxx",
    RetrieveCourseLessonsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the lesson to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.courseLessons.delete(id) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Permanently delete a lesson and remove it from its chapter.

Required permissions:
 - `courses:update`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courseLessons().delete(
    "lesn_xxxxxxxxxxxxx",
    DeleteCourseLessonsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the lesson to delete (e.g., "les_XXXXX").
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.courseLessons.update(id, request) -> CourseLesson</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Update a lesson's content, type, visibility, assessment questions, or media attachments.

Required permissions:
 - `courses:update`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courseLessons().update(
    "lesn_xxxxxxxxxxxxx",
    UpdateCourseLessonsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the lesson to update (e.g., "les_XXXXX").
    
</dd>
</dl>

<dl>
<dd>

**assessmentCompletionRequirement:** `Optional<UpdateCourseLessonsRequestAssessmentCompletionRequirement>` — The passing criteria for quiz or knowledge check lessons, such as minimum grade or correct answers.
    
</dd>
</dl>

<dl>
<dd>

**assessmentQuestions:** `Optional<List<UpdateCourseLessonsRequestAssessmentQuestionsItem>>` — The full list of assessment questions for quiz or knowledge check lessons. Replaces all existing questions.
    
</dd>
</dl>

<dl>
<dd>

**attachments:** `Optional<List<UpdateCourseLessonsRequestAttachmentsItem>>` — File attachments for the lesson such as PDFs or documents. Replaces all existing attachments.
    
</dd>
</dl>

<dl>
<dd>

**content:** `Optional<String>` — The Markdown content body of the lesson.
    
</dd>
</dl>

<dl>
<dd>

**daysFromCourseStartUntilUnlock:** `Optional<Integer>` — The number of days after a student starts the course before this lesson becomes accessible.
    
</dd>
</dl>

<dl>
<dd>

**embedId:** `Optional<String>` — The external video identifier for embedded content (e.g., a YouTube video ID or Loom share ID).
    
</dd>
</dl>

<dl>
<dd>

**embedType:** `Optional<EmbedTypes>` — The type of video embed for this lesson, such as YouTube or Loom.
    
</dd>
</dl>

<dl>
<dd>

**lessonType:** `Optional<LessonTypes>` — The content type of the lesson, such as video, text, quiz, or knowledge check.
    
</dd>
</dl>

<dl>
<dd>

**mainPdf:** `Optional<UpdateCourseLessonsRequestMainPdf>` — The primary PDF document attached to this lesson for student reference.
    
</dd>
</dl>

<dl>
<dd>

**maxAttempts:** `Optional<Integer>` — The maximum number of attempts a student is allowed for assessment lessons.
    
</dd>
</dl>

<dl>
<dd>

**muxAssetId:** `Optional<String>` — The identifier of a Mux video asset to attach to this lesson (e.g., "mux_XXXXX").
    
</dd>
</dl>

<dl>
<dd>

**thumbnail:** `Optional<UpdateCourseLessonsRequestThumbnail>` — The thumbnail image for the lesson in PNG, JPEG, or GIF format.
    
</dd>
</dl>

<dl>
<dd>

**title:** `Optional<String>` — The display title of the lesson (e.g., "Getting Started with APIs").
    
</dd>
</dl>

<dl>
<dd>

**visibility:** `Optional<LessonVisibilities>` — Controls whether this lesson is visible to students or hidden as a draft.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.courseLessons.markAsCompleted(lessonId) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Mark a lesson as completed for the current user after they finish the content.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courseLessons().markAsCompleted(
    "lesson_id",
    MarkAsCompletedCourseLessonsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**lessonId:** `String` — The unique identifier of the lesson to mark as completed (e.g., "les_XXXXX").
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.courseLessons.start(lessonId) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Record that the current user has started viewing a lesson, creating progress tracking records.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courseLessons().start(
    "lesson_id",
    StartCourseLessonsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**lessonId:** `String` — The unique identifier of the lesson the user is starting (e.g., "les_XXXXX").
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.courseLessons.submitAssessment(lessonId, request) -> SubmitAssessmentCourseLessonsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Submit answers for a quiz or knowledge check lesson and receive a graded result.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courseLessons().submitAssessment(
    "lesson_id",
    SubmitAssessmentCourseLessonsRequest
        .builder()
        .answers(
            Arrays.asList(
                SubmitAssessmentCourseLessonsRequestAnswersItem
                    .builder()
                    .questionId("question_id")
                    .build()
            )
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**lessonId:** `String` — The unique identifier of the quiz or knowledge check lesson to submit answers for (e.g., "les_XXXXX").
    
</dd>
</dl>

<dl>
<dd>

**answers:** `List<SubmitAssessmentCourseLessonsRequestAnswersItem>` — The list of answers to submit for each assessment question.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## CourseStudents
<details><summary><code>client.courseStudents.list() -> SyncPagingIterable&amp;lt;CourseStudentListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of students enrolled in a course, with optional name filtering.

Required permissions:
 - `courses:read`
 - `course_analytics:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courseStudents().list(
    ListCourseStudentsRequest
        .builder()
        .courseId("cors_xxxxxxxxxxxxx")
        .first(42)
        .last(42)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**courseId:** `String` — The unique identifier of the course to list enrolled students for.
    
</dd>
</dl>

<dl>
<dd>

**keyword:** `Optional<String>` — A search term to filter students by name or username.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.courseStudents.retrieve(id) -> CourseStudent</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing course student.

Required permissions:
 - `courses:read`
 - `course_analytics:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courseStudents().retrieve(
    "id",
    RetrieveCourseStudentsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the course student record to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Courses
<details><summary><code>client.courses.list() -> SyncPagingIterable&amp;lt;CourseListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of courses, filtered by either an experience or a company.

Required permissions:
 - `courses:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courses().list(
    ListCoursesRequest
        .builder()
        .first(42)
        .last(42)
        .experienceId("exp_xxxxxxxxxxxxxx")
        .companyId("biz_xxxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**experienceId:** `Optional<String>` — The unique identifier of the experience to list courses for.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `Optional<String>` — The unique identifier of the company to list courses for.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.courses.create(request) -> Course</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Create a new course within an experience, with optional chapters, lessons, and a certificate.

Required permissions:
 - `courses:update`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courses().create(
    CreateCoursesRequest
        .builder()
        .experienceId("exp_xxxxxxxxxxxxxx")
        .title("title")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**certificateAfterCompletionEnabled:** `Optional<Boolean>` — Whether the course awards students a PDF certificate after completing all lessons.
    
</dd>
</dl>

<dl>
<dd>

**experienceId:** `String` — The unique identifier of the experience to create the course in (e.g., "exp_XXXXX").
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<String>` — The decimal order position of the course within its experience. Use fractional values (e.g., "1.5") to place between existing courses.
    
</dd>
</dl>

<dl>
<dd>

**requireCompletingLessonsInOrder:** `Optional<Boolean>` — Whether students must complete each lesson sequentially before advancing to the next one.
    
</dd>
</dl>

<dl>
<dd>

**tagline:** `Optional<String>` — A short tagline displayed beneath the course title (e.g., "Master the fundamentals of design").
    
</dd>
</dl>

<dl>
<dd>

**thumbnail:** `Optional<CreateCoursesRequestThumbnail>` — The thumbnail image for the course in PNG, JPEG, or GIF format.
    
</dd>
</dl>

<dl>
<dd>

**title:** `String` — The display title of the course (e.g., "Introduction to Web Development").
    
</dd>
</dl>

<dl>
<dd>

**visibility:** `Optional<CourseVisibilities>` — Controls whether this course is visible to students or hidden as a draft.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.courses.retrieve(id) -> Course</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing course.

Required permissions:
 - `courses:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courses().retrieve(
    "cors_xxxxxxxxxxxxx",
    RetrieveCoursesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the course to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.courses.delete(id) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Permanently delete a course and all of its chapters, lessons, and student progress.

Required permissions:
 - `courses:update`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courses().delete(
    "cors_xxxxxxxxxxxxx",
    DeleteCoursesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the course to delete (e.g., "course_XXXXX").
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.courses.update(id, request) -> Course</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Update a course's title, description, visibility, thumbnail, or chapter ordering.

Required permissions:
 - `courses:update`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.courses().update(
    "cors_xxxxxxxxxxxxx",
    UpdateCoursesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the course to update (e.g., "course_XXXXX").
    
</dd>
</dl>

<dl>
<dd>

**certificateAfterCompletionEnabled:** `Optional<Boolean>` — Whether the course awards students a PDF certificate after completing all lessons.
    
</dd>
</dl>

<dl>
<dd>

**chapters:** `Optional<List<UpdateCoursesRequestChaptersItem>>` — A list of chapters with nested lessons to reorder or rename in bulk.
    
</dd>
</dl>

<dl>
<dd>

**description:** `Optional<String>` — A short description of the course displayed to students on the course page.
    
</dd>
</dl>

<dl>
<dd>

**language:** `Optional<Languages>` — The primary language spoken in the video content of the course.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<String>` — The decimal order position of the course within its experience. Use fractional values (e.g., "1.5") to place between existing courses.
    
</dd>
</dl>

<dl>
<dd>

**requireCompletingLessonsInOrder:** `Optional<Boolean>` — Whether students must complete each lesson sequentially before advancing to the next one.
    
</dd>
</dl>

<dl>
<dd>

**tagline:** `Optional<String>` — A short tagline displayed beneath the course title (e.g., "Master the fundamentals of design").
    
</dd>
</dl>

<dl>
<dd>

**thumbnail:** `Optional<UpdateCoursesRequestThumbnail>` — The thumbnail image for the course in PNG, JPEG, or GIF format.
    
</dd>
</dl>

<dl>
<dd>

**title:** `Optional<String>` — The display title of the course (e.g., "Introduction to Web Development").
    
</dd>
</dl>

<dl>
<dd>

**visibility:** `Optional<CourseVisibilities>` — Controls whether this course is visible to students or hidden as a draft.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Deposits
<details><summary><code>client.deposits.create(request) -> CreateDepositsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieve the deposit methods for an account, including crypto and bank transfer.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.deposits().create(
    CreateDepositsRequest
        .builder()
        .destination(
            CreateDepositsRequestDestination.of("destination")
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**amount:** `Optional<Double>` — Amount to prefill on hosted deposit page.
    
</dd>
</dl>

<dl>
<dd>

**destination:** `CreateDepositsRequestDestination` — Destination account ID or wallet address. Object form is supported for compatibility. Any business resolves by its account ID without authentication; a user account resolves only for that same authenticated user.
    
</dd>
</dl>

<dl>
<dd>

**metadata:** `Optional<Map<String, Object>>` — Metadata to include with the deposit response.
    
</dd>
</dl>

<dl>
<dd>

**network:** `Optional<CreateDepositsRequestNetwork>` — Destination network override. Defaults to the destination wallet's own network.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Dispute alerts
<details><summary><code>client.disputeAlerts.list() -> SyncPagingIterable&amp;lt;DisputeAlert&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the dispute alerts and early fraud warnings across the accounts you can read.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.disputeAlerts().list(
    ListDisputeAlertsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — Only alerts on this account's payments (`biz_` tag). Omit it to cover every account you can read.
    
</dd>
</dl>

<dl>
<dd>

**paymentId:** `Optional<String>` — Only alerts on this payment (`pay_` tag). A payment can carry several.
    
</dd>
</dl>

<dl>
<dd>

**type:** `Optional<ListDisputeAlertsRequestType>` — Only alerts of this kind. `early_fraud_warning` for issuer fraud reports, `dispute_alert` for pre-dispute notices, `rapid_dispute_resolution` for Visa RDR cases the network already closed.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of alerts to return (default 20, max 100).
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor; returns alerts after this position.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — The number of alerts to return from the end of the range.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — A cursor; returns alerts before this position.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListDisputeAlertsRequestOrder>` — The field to sort alerts by.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListDisputeAlertsRequestDirection>` — Sort direction.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Only alerts Whop received before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Only alerts Whop received after this ISO 8601 timestamp.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.disputeAlerts.retrieve(id) -> DisputeAlert</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a single dispute alert or early fraud warning by ID.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.disputeAlerts().retrieve(
    "id",
    RetrieveDisputeAlertsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The dispute alert ID, prefixed `dspa_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Disputes
<details><summary><code>client.disputes.list() -> SyncPagingIterable&amp;lt;Dispute&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the disputes across the accounts you can read.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.disputes().list(
    ListDisputesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — Only disputes filed against this account (`biz_` tag). Omit it to cover every account you can read.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of disputes to return (default 20, max 100).
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor; returns disputes after this position.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — The number of disputes to return from the end of the range.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — A cursor; returns disputes before this position.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListDisputesRequestOrder>` — The field to sort disputes by.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListDisputesRequestDirection>` — Sort direction.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<ListDisputesRequestStatusItem>` — Only disputes in these statuses. Repeat the parameter to pass several — one paginated list covers all of them. Covers both chargebacks and inquiries at each stage. A `needs_response` dispute whose evidence deadline has passed reports and filters as `under_review` instead.
    
</dd>
</dl>

<dl>
<dd>

**currency:** `Optional<String>` — Only disputes in this three-letter ISO currency.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Only disputes opened before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Only disputes opened after this ISO 8601 timestamp.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.disputes.summary() -> SummaryDisputesResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Totals up the same disputes the list returns, so you can build status tabs and totals without paging through them.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.disputes().summary(
    SummaryDisputesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**groups:** `Optional<SummaryDisputesRequestGroupsItem>` — Which breakdowns to return, keyed by these names under `groups`. Repeat the parameter to ask for several; omit it for all of them.
    
</dd>
</dl>

<dl>
<dd>

**accountId:** `Optional<String>` — Only disputes filed against this account (`biz_` tag). Omit it to cover every account you can read.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<SummaryDisputesRequestStatusItem>` — Only disputes in these statuses. Repeat the parameter to pass several. A `needs_response` dispute whose evidence deadline has passed reports and filters as `under_review` instead.
    
</dd>
</dl>

<dl>
<dd>

**currency:** `Optional<String>` — Only disputes in this three-letter ISO currency.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Only disputes opened before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Only disputes opened after this ISO 8601 timestamp.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.disputes.retrieve(id) -> Dispute</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a single dispute.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.disputes().retrieve(
    "id",
    RetrieveDisputesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The dispute ID (`dspt_` tag).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.disputes.update(id, request) -> Dispute</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Edits a dispute's evidence, while it is still editable. Sending it is a separate call.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.disputes().update(
    "id",
    UpdateDisputesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The dispute ID (`dspt_` tag).
    
</dd>
</dl>

<dl>
<dd>

**evidence:** `Optional<UpdateDisputesRequestEvidence>` — The evidence packet to send to the processor. Only the fields you provide are changed.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.disputes.submit(id) -> Dispute</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Sends a dispute's evidence to the payment processor. This is final — it cannot be edited or sent again.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.disputes().submit(
    "id",
    SubmitDisputesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The dispute ID (`dspt_` tag).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.disputes.submitEvidenceDispute(id) -> Dispute</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Submit a payment dispute to the payment processor for review. Once submitted, no further edits can be made.

Required permissions:
 - `payment:dispute`
 - `plan:basic:read`
 - `access_pass:basic:read`
 - `company:basic:read`
 - `payment:basic:read`
 - `member:email:read`
 - `member:basic:read`
 - `member:phone:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.disputes().submitEvidenceDispute(
    "dspt_xxxxxxxxxxxxx",
    SubmitEvidenceDisputeRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the dispute to submit to the payment processor for review.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.disputes.updateEvidenceDispute(id, request) -> Dispute</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Update a dispute with evidence data to attempt to win the dispute.

Required permissions:
 - `payment:dispute`
 - `plan:basic:read`
 - `access_pass:basic:read`
 - `company:basic:read`
 - `payment:basic:read`
 - `member:email:read`
 - `member:basic:read`
 - `member:phone:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.disputes().updateEvidenceDispute(
    "dspt_xxxxxxxxxxxxx",
    UpdateEvidenceDisputeRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the dispute to update.
    
</dd>
</dl>

<dl>
<dd>

**accessActivityLog:** `Optional<String>` — An IP access activity log showing the customer used the service.
    
</dd>
</dl>

<dl>
<dd>

**billingAddress:** `Optional<String>` — The billing address associated with the customer's payment method.
    
</dd>
</dl>

<dl>
<dd>

**cancellationPolicyAttachment:** `Optional<UpdateEvidenceDisputeRequestCancellationPolicyAttachment>` — A file upload containing the company's cancellation policy document.
    
</dd>
</dl>

<dl>
<dd>

**cancellationPolicyDisclosure:** `Optional<String>` — The company's cancellation policy text to submit as evidence.
    
</dd>
</dl>

<dl>
<dd>

**customerCommunicationAttachment:** `Optional<UpdateEvidenceDisputeRequestCustomerCommunicationAttachment>` — A file upload containing evidence of customer communication. Must be a JPEG, PNG, GIF, or PDF.
    
</dd>
</dl>

<dl>
<dd>

**customerEmailAddress:** `Optional<String>` — The email address of the customer associated with the disputed payment.
    
</dd>
</dl>

<dl>
<dd>

**customerName:** `Optional<String>` — The full name of the customer associated with the disputed payment.
    
</dd>
</dl>

<dl>
<dd>

**notes:** `Optional<String>` — Additional notes or context to submit as part of the dispute evidence.
    
</dd>
</dl>

<dl>
<dd>

**productDescription:** `Optional<String>` — A description of the product or service that was provided to the customer.
    
</dd>
</dl>

<dl>
<dd>

**refundPolicyAttachment:** `Optional<UpdateEvidenceDisputeRequestRefundPolicyAttachment>` — A file upload containing the company's refund policy document.
    
</dd>
</dl>

<dl>
<dd>

**refundPolicyDisclosure:** `Optional<String>` — The company's refund policy text to submit as evidence.
    
</dd>
</dl>

<dl>
<dd>

**refundRefusalExplanation:** `Optional<String>` — An explanation of why the refund request was refused.
    
</dd>
</dl>

<dl>
<dd>

**serviceDate:** `Optional<String>` — The date when the product or service was delivered to the customer.
    
</dd>
</dl>

<dl>
<dd>

**uncategorizedAttachment:** `Optional<UpdateEvidenceDisputeRequestUncategorizedAttachment>` — A file upload for evidence that does not fit into the other categories.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.disputes.uploadEvidence(id, request) -> Dispute</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Replaces the full set of uploaded evidence documents on a dispute, beyond the four fixed evidence slots. Send the files as multipart file parts to upload and attach in one call, or reference files already stored by `id`/`direct_upload_id`. Send every document the packet should carry — up to 10, 10MB each and 25MB in total; an empty list removes them all. Accepted content types: application/pdf, application/json, image/jpeg, image/png, image/webp — any other type is rejected.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.disputes().uploadEvidence(
    "id",
    UploadEvidenceDisputesRequest
        .builder()
        .documents(
            Arrays.asList(
                UploadEvidenceDisputesRequestDocumentsItem
                    .builder()
                    .documentType(UploadEvidenceDisputesRequestDocumentsItemDocumentType.RETURN_POLICY)
                    .build()
            )
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The dispute ID (`dspt_` tag).
    
</dd>
</dl>

<dl>
<dd>

**documents:** `List<UploadEvidenceDisputesRequestDocumentsItem>` — The full set of evidence documents the dispute should carry. Replaces all previously uploaded documents.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## DmChannels
<details><summary><code>client.dmChannels.list() -> SyncPagingIterable&amp;lt;DmChannelListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of DM channels for the currently authenticated user, sorted by most recently active.

Required permissions:
 - `dms:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.dmChannels().list(
    ListDmChannelsRequest
        .builder()
        .first(42)
        .last(42)
        .companyId("biz_xxxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `Optional<String>` — The unique identifier of a company to filter DM channels by. Only returns channels scoped to this company.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.dmChannels.create(request) -> DmChannel</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Create a new DM channel between two or more users, optionally scoped to a specific company. Returns the existing channel if one already exists.

Required permissions:
 - `dms:channel:manage`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.dmChannels().create(
    CreateDmChannelsRequest
        .builder()
        .withUserIds(
            Arrays.asList("with_user_ids")
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**companyId:** `Optional<String>` — The unique identifier of the company to scope this DM channel to. When set, the channel is visible only within that company context.
    
</dd>
</dl>

<dl>
<dd>

**customName:** `Optional<String>` — A custom display name for the DM channel. For example, 'Project Discussion'.
    
</dd>
</dl>

<dl>
<dd>

**notificationsEnabled:** `Optional<Boolean>` — Whether Whop app notifications are enabled for this direct message channel. Webhooks still fire.
    
</dd>
</dl>

<dl>
<dd>

**withUserIds:** `List<String>` — The list of user identifiers to include in the DM channel. Each entry can be an email, username, or user ID (e.g. 'user_xxxxx').
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.dmChannels.retrieve(id) -> DmChannel</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing DM channel.

Required permissions (one of):
 - `dms:read`
 - `support_chat:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.dmChannels().retrieve(
    "id",
    RetrieveDmChannelsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the DM channel to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.dmChannels.delete(id) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Permanently delete a DM channel and all of its messages. Only an admin of the channel can perform this action.

Required permissions (one of):
 - `dms:channel:manage`
 - `support_chat:create`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.dmChannels().delete(
    "id",
    DeleteDmChannelsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the DM channel to delete.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.dmChannels.update(id, request) -> DmChannel</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Update the settings of an existing DM channel, such as its display name. Only an admin of the channel can perform this action.

Required permissions (one of):
 - `dms:channel:manage`
 - `support_chat:create`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.dmChannels().update(
    "id",
    UpdateDmChannelsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the DM channel to update.
    
</dd>
</dl>

<dl>
<dd>

**customName:** `Optional<String>` — A new custom display name for the DM channel. For example, 'Project Discussion'.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## DmMembers
<details><summary><code>client.dmMembers.list() -> SyncPagingIterable&amp;lt;DmMemberListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of members in a specific DM channel, sorted by the date they were added.

Required permissions (one of):
 - `dms:read`
 - `support_chat:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.dmMembers().list(
    ListDmMembersRequest
        .builder()
        .channelId("channel_id")
        .first(42)
        .last(42)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**channelId:** `String` — The unique identifier of the DM channel to list members for.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.dmMembers.create(request) -> DmMember</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Add a new user to an existing DM channel. Only an admin of the channel can add members.

Required permissions (one of):
 - `dms:message:manage`
 - `support_chat:message:create`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.dmMembers().create(
    CreateDmMembersRequest
        .builder()
        .channelId("channel_id")
        .userId("user_xxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**channelId:** `String` — The unique identifier of the DM channel to add the new member to.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `String` — The unique identifier of the user to add to the DM channel. For example, 'user_xxxxx'.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.dmMembers.retrieve(id) -> DmMember</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing DM member.

Required permissions (one of):
 - `dms:read`
 - `support_chat:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.dmMembers().retrieve(
    "id",
    RetrieveDmMembersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the DM channel member to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.dmMembers.delete(id) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Remove a user from a DM channel. An admin can remove any member, and a member can remove themselves.

Required permissions (one of):
 - `dms:read`
 - `support_chat:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.dmMembers().delete(
    "id",
    DeleteDmMembersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the DM channel member to remove.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.dmMembers.update(id, request) -> DmMember</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Update a DM channel member's settings, such as their notification preferences or membership status.

Required permissions (one of):
 - `dms:read`
 - `support_chat:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.dmMembers().update(
    "id",
    UpdateDmMembersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the DM channel member to update.
    
</dd>
</dl>

<dl>
<dd>

**notificationPreference:** `Optional<DmsFeedMemberNotificationPreferences>` — The notification setting for this member, controlling how they receive alerts for new messages in this channel.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<DmsFeedMemberStatuses>` — The membership status for this member in the DM channel.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Entries
<details><summary><code>client.entries.list() -> SyncPagingIterable&amp;lt;EntryListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of waitlist entries for a company, with optional filtering by product, plan, status, and creation date.

Required permissions:
 - `plan:waitlist:read`
 - `member:email:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.entries().list(
    ListEntriesRequest
        .builder()
        .companyId("biz_xxxxxxxxxxxxxx")
        .first(42)
        .last(42)
        .createdBefore(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .createdAfter(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `String` — The unique identifier of the company to list waitlist entries for.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<Direction>` 
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<EntriesSortableColumns>` 
    
</dd>
</dl>

<dl>
<dd>

**productIds:** `Optional<String>` — Filter entries to only those for specific products.
    
</dd>
</dl>

<dl>
<dd>

**planIds:** `Optional<String>` — Filter entries to only those for specific plans.
    
</dd>
</dl>

<dl>
<dd>

**statuses:** `Optional<EntryStatus>` — Filter entries by their current status.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<OffsetDateTime>` — Only return entries created before this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<OffsetDateTime>` — Only return entries created after this timestamp.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.entries.retrieve(id) -> Entry</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing waitlist entry.

Required permissions:
 - `plan:waitlist:read`
 - `member:email:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.entries().retrieve(
    "entry_xxxxxxxxxxxx",
    RetrieveEntriesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the waitlist entry to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.entries.approve(id) -> ApproveEntriesResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Approve a pending waitlist entry, triggering the checkout process to grant the user access to the plan.

Required permissions:
 - `plan:waitlist:manage`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.entries().approve(
    "entry_xxxxxxxxxxxx",
    ApproveEntriesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the waitlist entry to approve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.entries.deny(id) -> Entry</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Deny a pending waitlist entry, preventing the user from gaining access to the plan.

Required permissions:
 - `plan:waitlist:manage`
 - `plan:basic:read`
 - `member:email:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.entries().deny(
    "entry_xxxxxxxxxxxx",
    DenyEntriesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the waitlist entry to deny.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Events
<details><summary><code>client.events.list() -> SyncPagingIterable&amp;lt;ListEventsResponseDataItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists identity-linked events, most recent first by default. Pass identifier for one person's journey, or omit it to list events for an account within an explicit time range. Pass direction=asc to read a journey forwards from where it starts. Events are shaped like the POST /events intake: attribution in context, identity in user.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.events().list(
    ListEventsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**identifier:** `Optional<String>` — Any hard identifier of the person: a person ID (prsn_*), user ID, email, phone number, or a tracking cookie value (wuid, anonymous ID, fbp/fbc/ttp/ga). Omit to list recent events for the account.
    
</dd>
</dl>

<dl>
<dd>

**accountId:** `Optional<String>` — Account ID, prefixed `biz_`. Optional for account API keys; required for credentials that can access multiple accounts.
    
</dd>
</dl>

<dl>
<dd>

**from:** `Optional<OffsetDateTime>` — Start of the time range as an ISO 8601 timestamp. Required when identifier is omitted.
    
</dd>
</dl>

<dl>
<dd>

**to:** `Optional<OffsetDateTime>` — End of the time range as an ISO 8601 timestamp. Required when identifier is omitted; otherwise defaults to now.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of events to return.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor for fetching events after a previous page.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — A cursor for fetching events before a later page.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListEventsRequestDirection>` — The order events are returned in by time. Defaults to desc (most recent first); asc reads a journey forwards from where it starts. after and before always page forwards and backwards through that order.
    
</dd>
</dl>

<dl>
<dd>

**event:** `Optional<String>` — Full event names to filter by, comma-separated (payment.completed, pixel.lead, pixel.page, pixel.custom:<name>) — the same vocabulary the events / people metrics use.
    
</dd>
</dl>

<dl>
<dd>

**source:** `Optional<String>` — Canonical source path, exact or with a trailing :* prefix (whop:<campaign>:*, ext:meta:*, referrer:<domain>, direct). Restricts the list to conversion targets attributed to that source — the debuggability twin of a metric cell's source parameter.
    
</dd>
</dl>

<dl>
<dd>

**attributionModel:** `Optional<ListEventsRequestAttributionModel>` — Attribution model for the source filter (defaults to last_touch).
    
</dd>
</dl>

<dl>
<dd>

**country:** `Optional<String>` — Country codes to filter by, comma-separated.
    
</dd>
</dl>

<dl>
<dd>

**city:** `Optional<String>` — Cities to filter by, comma-separated.
    
</dd>
</dl>

<dl>
<dd>

**device:** `Optional<String>` — Device families to filter by, comma-separated (e.g. iPhone, Mac).
    
</dd>
</dl>

<dl>
<dd>

**browser:** `Optional<String>` — Browser families to filter by, comma-separated (e.g. Chrome, Mobile Safari).
    
</dd>
</dl>

<dl>
<dd>

**os:** `Optional<String>` — Operating system families to filter by, comma-separated (e.g. iOS, Windows).
    
</dd>
</dl>

<dl>
<dd>

**utmSource:** `Optional<String>` — utm_source values to filter by, comma-separated.
    
</dd>
</dl>

<dl>
<dd>

**hostname:** `Optional<String>` — Page hostnames to filter by, comma-separated.
    
</dd>
</dl>

<dl>
<dd>

**page:** `Optional<String>` — Page paths to filter by, comma-separated.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.events.create(request) -> CreateEventsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Tracks a conversion or engagement event for an account.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.events().create(
    CreateEventsRequest
        .builder()
        .accountId("biz_xxxxxxxxxxxxxx")
        .eventName("coating_deposit_paid")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `String` — The account to associate with this event.
    
</dd>
</dl>

<dl>
<dd>

**actionSource:** `Optional<CreateEventsRequestActionSource>` — Where the event originated.
    
</dd>
</dl>

<dl>
<dd>

**context:** `Optional<CreateEventsRequestContext>` — Tracking and attribution context.
    
</dd>
</dl>

<dl>
<dd>

**currency:** `Optional<CreateEventsRequestCurrency>` — ISO 4217 currency code.
    
</dd>
</dl>

<dl>
<dd>

**customName:** `Optional<String>` — Custom event name when event_name is 'custom'. Maximum 35 chars for this value.
    
</dd>
</dl>

<dl>
<dd>

**duration:** `Optional<Integer>` — For 'leave' events: milliseconds the visitor spent on the page.
    
</dd>
</dl>

<dl>
<dd>

**eventId:** `Optional<String>` — Client-provided identifier for deduplication. Generated if omitted.
    
</dd>
</dl>

<dl>
<dd>

**eventName:** `String` 

The type of event.

Use a standard event (lead, submit_application, contact, complete_registration, schedule, view_content, add_to_cart) or pass your own name directly for a custom event.
    
</dd>
</dl>

<dl>
<dd>

**eventTime:** `Optional<OffsetDateTime>` — When the event occurred. Defaults to now.
    
</dd>
</dl>

<dl>
<dd>

**planId:** `Optional<String>` — The plan associated with the event.
    
</dd>
</dl>

<dl>
<dd>

**productId:** `Optional<String>` — The product associated with the event.
    
</dd>
</dl>

<dl>
<dd>

**referrerUrl:** `Optional<String>` — The referring URL.
    
</dd>
</dl>

<dl>
<dd>

**resumed:** `Optional<Boolean>` — For 'page' events: true when the page was restored from the back/forward cache.
    
</dd>
</dl>

<dl>
<dd>

**source:** `Optional<String>` — For 'identify' events: where the identity was captured (url, form, manual, iframe).
    
</dd>
</dl>

<dl>
<dd>

**title:** `Optional<String>` — For 'page' events: the document title.
    
</dd>
</dl>

<dl>
<dd>

**url:** `Optional<String>` — The URL where the event occurred.
    
</dd>
</dl>

<dl>
<dd>

**user:** `Optional<CreateEventsRequestUser>` — User identity and profile data.
    
</dd>
</dl>

<dl>
<dd>

**value:** `Optional<Double>` — Monetary value associated with the event.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.events.pulse() -> SyncPagingIterable&amp;lt;PulseEventsResponseDataItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a fully anonymized feed of recent platform-wide money movement, most recent first: purchases, affiliate commissions, card and ad spend, app revenue, off-platform sales, wallet deposits, card loads, claimed drops, transfers between accounts, and referral bonuses. Items carry only a `type`, the underlying event name, a USD amount, a coarse location under `user`, and a timestamp coarsened to the start of the minute; missing fields are omitted, not nulled. The payload is identical for every caller; no auth is required.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.events().pulse(
    PulseEventsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**event:** `Optional<String>` — Filter to one or more types, comma separated — for example `purchase,card_spend`. These are the item's `type`, not its `event_name`: several types share the `ledger_line.created` event name. Omit for every type in the feed. Values outside the feed's own set are rejected.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of events to return.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor for fetching events after a previous page.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — A cursor for fetching events before a later page.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.events.validatePixel(request) -> PixelValidation</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Checks whether the Whop pixel is installed for an account. Recent pixel events count as proof on their own, so an account that has sent data lately comes back installed without a `url`. Pass a `url` and events from that page settle it; conversion events are also read across the hostname because they commonly fire on a later confirmation page. If the requested page hasn't sent any events lately, it is fetched and read for the pixel and conversion events wired on it. `installed` is only true when the pixel was actually seen — in the account's events or in the page.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.events().validatePixel(
    ValidatePixelEventsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — Account to check. Defaults to the authenticated account.
    
</dd>
</dl>

<dl>
<dd>

**url:** `Optional<String>` — A page to read for the pixel, e.g. an ad destination. Omit it to check the account from its events alone.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Experiences
<details><summary><code>client.experiences.list() -> SyncPagingIterable&amp;lt;ExperienceListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of experiences belonging to a company, with optional filtering by product and app.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.experiences().list(
    ListExperiencesRequest
        .builder()
        .companyId("biz_xxxxxxxxxxxxxx")
        .first(42)
        .last(42)
        .productId("prod_xxxxxxxxxxxxx")
        .appId("app_xxxxxxxxxxxxxx")
        .createdBefore(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .createdAfter(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `String` — The unique identifier of the company to list experiences for.
    
</dd>
</dl>

<dl>
<dd>

**productId:** `Optional<String>` — Filter to only experiences attached to this product identifier.
    
</dd>
</dl>

<dl>
<dd>

**appId:** `Optional<String>` — Filter to only experiences powered by this app identifier.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<OffsetDateTime>` — Only return experiences created before this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<OffsetDateTime>` — Only return experiences created after this timestamp.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.experiences.create(request) -> Experience</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Required permissions:
 - `experience:create`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.experiences().create(
    CreateExperiencesRequest
        .builder()
        .appId("app_xxxxxxxxxxxxxx")
        .companyId("biz_xxxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**appId:** `String` — The unique identifier of the app that powers this experience.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `String` — The unique identifier of the company to create this experience for.
    
</dd>
</dl>

<dl>
<dd>

**isPublic:** `Optional<Boolean>` — Whether the experience is publicly accessible without a membership.
    
</dd>
</dl>

<dl>
<dd>

**logo:** `Optional<CreateExperiencesRequestLogo>` — A logo image displayed alongside the experience name.
    
</dd>
</dl>

<dl>
<dd>

**name:** `Optional<String>` — The display name of the experience. Defaults to the app's name if not provided.
    
</dd>
</dl>

<dl>
<dd>

**notificationsEnabled:** `Optional<Boolean>` — Whether Whop app notifications are enabled for this experience. Webhooks still fire.
    
</dd>
</dl>

<dl>
<dd>

**sectionId:** `Optional<String>` — The unique identifier of the section to place the experience in.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.experiences.retrieve(id) -> Experience</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing experience.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.experiences().retrieve(
    "exp_xxxxxxxxxxxxxx",
    RetrieveExperiencesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the experience.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.experiences.delete(id) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Required permissions:
 - `experience:delete`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.experiences().delete(
    "exp_xxxxxxxxxxxxxx",
    DeleteExperiencesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the experience to delete.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.experiences.update(id, request) -> Experience</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Required permissions:
 - `experience:update`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.experiences().update(
    "exp_xxxxxxxxxxxxxx",
    UpdateExperiencesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the experience to update.
    
</dd>
</dl>

<dl>
<dd>

**accessLevel:** `Optional<ExperienceAccessLevels>` — The access level of the experience.
    
</dd>
</dl>

<dl>
<dd>

**isPublic:** `Optional<Boolean>` — Whether the experience is publicly accessible without a membership.
    
</dd>
</dl>

<dl>
<dd>

**logo:** `Optional<UpdateExperiencesRequestLogo>` — A logo image displayed alongside the experience name.
    
</dd>
</dl>

<dl>
<dd>

**name:** `Optional<String>` — The display name of the experience.
    
</dd>
</dl>

<dl>
<dd>

**notificationsEnabled:** `Optional<Boolean>` — Whether Whop app notifications are enabled for this experience. Webhooks still fire.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<String>` — The position of the experience within its section for display ordering.
    
</dd>
</dl>

<dl>
<dd>

**sectionId:** `Optional<String>` — The unique identifier of the section to move the experience into.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.experiences.attach(id, request) -> Experience</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Attach an experience to a product, making it accessible to the product's customers.

Required permissions:
 - `experience:attach`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.experiences().attach(
    "exp_xxxxxxxxxxxxxx",
    AttachExperiencesRequest
        .builder()
        .productId("prod_xxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the experience to attach.
    
</dd>
</dl>

<dl>
<dd>

**productId:** `String` — The unique identifier of the product to attach the experience to.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.experiences.detach(id, request) -> Experience</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Detach an experience from a product, removing customer access to it through that product.

Required permissions:
 - `experience:detach`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.experiences().detach(
    "exp_xxxxxxxxxxxxxx",
    DetachExperiencesRequest
        .builder()
        .productId("prod_xxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the experience to detach.
    
</dd>
</dl>

<dl>
<dd>

**productId:** `String` — The unique identifier of the product to detach the experience from.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.experiences.duplicate(id, request) -> Experience</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Duplicates an existing experience. The name will be copied, unless provided. The new experience will be attached to the same products as the original experience.
If duplicating a Forum or Chat experience, the new experience will have the same settings as the original experience, e.g. who can post, who can comment, etc.
No content, e.g. posts, messages, lessons from within the original experience will be copied.


Required permissions:
 - `experience:create`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.experiences().duplicate(
    "exp_xxxxxxxxxxxxxx",
    DuplicateExperiencesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the experience to duplicate.
    
</dd>
</dl>

<dl>
<dd>

**name:** `Optional<String>` — The display name for the duplicated experience. Defaults to the original experience's name.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Exports
<details><summary><code>client.exports.list() -> ListExportsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the exports requested for an account, newest first. Only exports of resources the credential is allowed to export are returned.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.exports().list(
    ListExportsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The account to list exports for, prefixed `biz_`. Defaults to the credential's account.
    
</dd>
</dl>

<dl>
<dd>

**resource:** `Optional<ListExportsRequestResource>` — Only return exports of this resource.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<ListExportsRequestStatus>` — Only return exports in this status.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Only return exports created at or after this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Only return exports created at or before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListExportsRequestOrder>` — The field to sort by.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListExportsRequestDirection>` — The sort direction.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.exports.create(request) -> Export</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Starts an asynchronous CSV export of a resource for an account. Returns the export in `pending`; poll `GET /exports/{id}` until `download_url` is set.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.exports().create(
    CreateExportsRequest
        .builder()
        .resource(CreateExportsRequestResource.AD_CAMPAIGNS)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The account to export from, prefixed `biz_`. Defaults to the credential's account.
    
</dd>
</dl>

<dl>
<dd>

**columns:** `Optional<List<String>>` — Column keys to include. Empty means all columns for the resource.
    
</dd>
</dl>

<dl>
<dd>

**filters:** `Optional<Map<String, Object>>` — Resource-specific filters. For native REST resources (`payouts`, `transfers`, `memberships`) these are the resource's own list query params; for dashboard tables they mirror the dashboard table filters.
    
</dd>
</dl>

<dl>
<dd>

**resource:** `CreateExportsRequestResource` — The resource to export, e.g. `payouts`, `receipts`, or `members`.
    
</dd>
</dl>

<dl>
<dd>

**timezone:** `Optional<String>` — IANA timezone for date columns, e.g. `America/New_York`. Defaults to `UTC`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.exports.retrieve(id) -> Export</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Fetches an export's status and, once complete, its download link.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.exports().retrieve(
    "id",
    RetrieveExportsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The export ID, prefixed `exprt_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## FeeMarkups
<details><summary><code>client.feeMarkups.list() -> SyncPagingIterable&amp;lt;FeeMarkupListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of fee markups configured for a company. If the company is a platform account, returns the platform default markups.

Required permissions:
 - `company:update_child_fees`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.feeMarkups().list(
    ListFeeMarkupsRequest
        .builder()
        .companyId("biz_xxxxxxxxxxxxxx")
        .first(42)
        .last(42)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `String` — The unique identifier of the company to list fee markups for. Pass a platform account identifier to retrieve platform default markups.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.feeMarkups.create(request) -> FeeMarkup</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Create or update a fee markup for a company. If a markup for the specified fee type already exists, it will be updated with the new values.

Required permissions:
 - `company:update_child_fees`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.feeMarkups().create(
    CreateFeeMarkupsRequest
        .builder()
        .companyId("biz_xxxxxxxxxxxxxx")
        .feeType(FeeMarkupTypes.CRYPTO_WITHDRAWAL_MARKUP)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**companyId:** `String` — The unique identifier of the company to create or update the fee markup for.
    
</dd>
</dl>

<dl>
<dd>

**feeType:** `FeeMarkupTypes` — The type of fee this markup applies to, such as processing or platform fees.
    
</dd>
</dl>

<dl>
<dd>

**fixedFeeUsd:** `Optional<Double>` — The fixed fee amount in USD to charge per transaction. Must be between 0 and 50.
    
</dd>
</dl>

<dl>
<dd>

**metadata:** `Optional<Map<String, Object>>` — Custom key-value metadata to attach to this fee markup.
    
</dd>
</dl>

<dl>
<dd>

**notes:** `Optional<String>` — Internal notes about this fee markup for record-keeping purposes.
    
</dd>
</dl>

<dl>
<dd>

**percentageFee:** `Optional<Double>` — The percentage fee to charge per transaction. Must be between 0 and 25.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.feeMarkups.delete(id) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Delete a fee markup configuration for a company. This removes the custom fee override and reverts to the parent company's default fees.

Required permissions:
 - `company:update_child_fees`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.feeMarkups().delete(
    "id",
    DeleteFeeMarkupsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the fee markup to delete.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Files
<details><summary><code>client.files.create(request) -> CreateFilesResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Create a new file record and receive a presigned URL for uploading content to S3.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.files().create(
    CreateFilesRequest
        .builder()
        .filename("filename")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**filename:** `String` — The name of the file including its extension (e.g., "photo.png" or "document.pdf").
    
</dd>
</dl>

<dl>
<dd>

**visibility:** `Optional<FileVisibility>` — Controls whether the file is publicly accessible via CDN or requires authentication. Defaults to private.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.files.retrieve(id) -> File</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing file.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.files().retrieve(
    "file_xxxxxxxxxxxxx",
    RetrieveFilesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the file to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## FinancialActivity
<details><summary><code>client.financialActivity.list() -> ListFinancialActivityResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns an account's or user's activity feed: every movement of money in or out.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.financialActivity().list(
    ListFinancialActivityRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The owning account ID (a biz_ identifier). Provide this or user_id.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — The owning user ID (a user_ identifier). Provide this or account_id.
    
</dd>
</dl>

<dl>
<dd>

**includeOwnedAccounts:** `Optional<Boolean>` — When true, aggregates the authenticated user's personal ledger with the businesses they own (owner role with balance read) into one feed. Requires user_id to be the authenticated user; cannot be combined with account_id or the settlement-date filters. Each returned row includes the owning `account`.
    
</dd>
</dl>

<dl>
<dd>

**includeResource:** `Optional<Boolean>` — Whether to include the `resource` field in the response or not. Consider passing `false` if you need a fast response without as many rich details.
    
</dd>
</dl>

<dl>
<dd>

**lineTypes:** `Optional<ListFinancialActivityRequestLineTypesItem>` — Optional ledger line categories to include. Some categories (for example `onchain_deposit`, which covers inbound crypto deposits such as MoonPay onramps) are only returned when explicitly requested here.
    
</dd>
</dl>

<dl>
<dd>

**currency:** `Optional<String>` — Optional currency code filter, for example `usd`.
    
</dd>
</dl>

<dl>
<dd>

**postedAfter:** `Optional<OffsetDateTime>` — Only include rows posted after this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**postedBefore:** `Optional<OffsetDateTime>` — Only include rows posted before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**availableAfter:** `Optional<String>` — Only include rows whose funds became withdrawable on or after this `YYYY-MM-DD` settlement date (UTC), distinct from posted_at. Requires currency.
    
</dd>
</dl>

<dl>
<dd>

**availableBefore:** `Optional<String>` — Only include rows whose funds became withdrawable on or before this `YYYY-MM-DD` settlement date (UTC). Set equal to available_after for a single day. Requires currency.
    
</dd>
</dl>

<dl>
<dd>

**limit:** `Optional<Integer>` — Maximum number of rows to return.
    
</dd>
</dl>

<dl>
<dd>

**cursor:** `Optional<String>` — Cursor returned by the previous page.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Ledgers
<details><summary><code>client.ledgers.getFinancialReport() -> GetFinancialReportResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a financial report — balance activity, income statement, or balance summary — for an account over a date range.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.ledgers().getFinancialReport(
    GetFinancialReportRequest
        .builder()
        .accountId("account_id")
        .reportType(GetFinancialReportRequestReportType.BALANCE_SUMMARY)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `String` — The owning account ID (a biz_ identifier), or `global` for a platform-wide report across all ledger accounts (requires internal admin access).
    
</dd>
</dl>

<dl>
<dd>

**reportType:** `GetFinancialReportRequestReportType` — The type of financial report to generate.
    
</dd>
</dl>

<dl>
<dd>

**currency:** `Optional<String>` — Filter rows to this currency, for example `usd`. Defaults to `usd` unless `in_currency` is provided.
    
</dd>
</dl>

<dl>
<dd>

**inCurrency:** `Optional<String>` — Aggregate all activity into this display currency via FX conversion.
    
</dd>
</dl>

<dl>
<dd>

**fromDate:** `Optional<String>` — Start of the report window as an ISO 8601 timestamp (UTC). Required for platform-wide (global) reports.
    
</dd>
</dl>

<dl>
<dd>

**toDate:** `Optional<String>` — End of the report window as an ISO 8601 timestamp (UTC). Required for platform-wide (global) reports.
    
</dd>
</dl>

<dl>
<dd>

**groupBy:** `Optional<GetFinancialReportRequestGroupBy>` — Grouping granularity for report rows.
    
</dd>
</dl>

<dl>
<dd>

**timezone:** `Optional<String>` — IANA timezone (for example `America/New_York`) used to bucket report periods and to interpret calendar-day boundaries for balance snapshots. Defaults to UTC. from_date/to_date remain exact instants regardless of this setting.
    
</dd>
</dl>

<dl>
<dd>

**cumulative:** `Optional<Boolean>` — Platform-wide (global) reports only: when true, return cumulative balances as of to_date (all history, no lower bound) instead of activity within the period.
    
</dd>
</dl>

<dl>
<dd>

**scopeAccountId:** `Optional<String>` — Platform-wide (global) reports only: narrow the report to ledger lines on the ledger account owned by this account ID (a biz_ identifier). Ignored unless account_id is `global`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## ForumPosts
<details><summary><code>client.forumPosts.list() -> SyncPagingIterable&amp;lt;ForumPostListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of forum posts within a specific experience, with optional filtering by parent post or pinned status.

Required permissions:
 - `forum:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.forumPosts().list(
    ListForumPostsRequest
        .builder()
        .experienceId("exp_xxxxxxxxxxxxxx")
        .first(42)
        .last(42)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**experienceId:** `String` — The unique identifier of the experience to list forum posts for.
    
</dd>
</dl>

<dl>
<dd>

**includeBountyAnchors:** `Optional<Boolean>` — Whether to include top-level bounty discussion anchors as rich forum items.
    
</dd>
</dl>

<dl>
<dd>

**parentId:** `Optional<String>` — The unique identifier of a parent post to list comments for. When set, returns replies to that post.
    
</dd>
</dl>

<dl>
<dd>

**pinned:** `Optional<Boolean>` — Whether to filter for only pinned posts. Set to true to return only pinned posts.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.forumPosts.create(request) -> ForumPost</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Create a new forum post or comment within an experience. Supports text content, attachments, polls, paywalling, and pinning. Pass experience_id 'public' with a company_id to post to a company's public forum.

Required permissions:
 - `forum:post:create`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.forumPosts().create(
    CreateForumPostsRequest
        .builder()
        .experienceId("exp_xxxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**attachments:** `Optional<List<CreateForumPostsRequestAttachmentsItem>>` — A list of file attachments to include with the post, such as images or videos.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `Optional<String>` — The unique identifier of the company whose public forum to post in. Required when experience_id is 'public'. For example, 'biz_xxxxx'.
    
</dd>
</dl>

<dl>
<dd>

**content:** `Optional<String>` — The main body of the post in Markdown format. For example, 'Check out this **update**'. Hidden if the post is paywalled and the viewer has not purchased access.
    
</dd>
</dl>

<dl>
<dd>

**experienceId:** `String` — The unique identifier of the experience to create this post in. For example, 'exp_xxxxx'. Pass 'public' along with company_id to automatically use the company's public forum.
    
</dd>
</dl>

<dl>
<dd>

**isMention:** `Optional<Boolean>` — Whether to send this post as a mention notification to all users in the experience who have mentions enabled.
    
</dd>
</dl>

<dl>
<dd>

**parentId:** `Optional<String>` — The unique identifier of the parent post to comment on. Omit this field to create a top-level post.
    
</dd>
</dl>

<dl>
<dd>

**paywallAmount:** `Optional<Double>` — The price to unlock this post in the specified paywall currency. For example, 5.00 for $5.00. When set, users must purchase access to view the post content.
    
</dd>
</dl>

<dl>
<dd>

**paywallCurrency:** `Optional<Currencies>` — The currency for the paywall price on this post. When set along with paywall_amount, users must purchase access to view the post content.
    
</dd>
</dl>

<dl>
<dd>

**pinned:** `Optional<Boolean>` — Whether this post should be pinned to the top of the forum.
    
</dd>
</dl>

<dl>
<dd>

**poll:** `Optional<CreateForumPostsRequestPoll>` — A poll to attach to this post, allowing members to vote on options.
    
</dd>
</dl>

<dl>
<dd>

**richContent:** `Optional<String>` — The rich content of the post in Tiptap JSON format. When provided, takes priority over the markdown content field for rendering.
    
</dd>
</dl>

<dl>
<dd>

**title:** `Optional<String>` — The title of the post, displayed prominently at the top. Required for paywalled posts as it remains visible to non-purchasers.
    
</dd>
</dl>

<dl>
<dd>

**visibility:** `Optional<ForumPostVisibilityTypes>` — Controls who can see this forum post, such as members only or public.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.forumPosts.retrieve(id) -> ForumPost</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing forum post.

Required permissions:
 - `forum:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.forumPosts().retrieve(
    "id",
    RetrieveForumPostsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the forum post to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.forumPosts.update(id, request) -> ForumPost</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Edit the content, attachments, pinned status, or visibility of an existing forum post or comment.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.forumPosts().update(
    "id",
    UpdateForumPostsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the forum post to update.
    
</dd>
</dl>

<dl>
<dd>

**attachments:** `Optional<List<UpdateForumPostsRequestAttachmentsItem>>` — A replacement list of file attachments for this post, such as images or videos.
    
</dd>
</dl>

<dl>
<dd>

**content:** `Optional<String>` — The updated body of the post in Markdown format. For example, 'Check out this **update**'. Hidden if the post is paywalled and the viewer has not purchased access.
    
</dd>
</dl>

<dl>
<dd>

**isPinned:** `Optional<Boolean>` — Whether this post should be pinned to the top of the forum. Only top-level posts can be pinned, not comments.
    
</dd>
</dl>

<dl>
<dd>

**title:** `Optional<String>` — The updated title of the post, displayed prominently at the top. Required for paywalled posts as it remains visible to non-purchasers.
    
</dd>
</dl>

<dl>
<dd>

**visibility:** `Optional<ForumPostVisibilityTypes>` — Controls who can see this forum post, such as members only or public.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Forums
<details><summary><code>client.forums.list() -> SyncPagingIterable&amp;lt;ForumListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of forums within a specific company, with optional filtering by product.

Required permissions:
 - `forum:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.forums().list(
    ListForumsRequest
        .builder()
        .companyId("biz_xxxxxxxxxxxxxx")
        .first(42)
        .last(42)
        .productId("prod_xxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `String` — The unique identifier of the company to list forums for.
    
</dd>
</dl>

<dl>
<dd>

**productId:** `Optional<String>` — The unique identifier of a product to filter by. When set, only forums connected to this product are returned.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.forums.retrieve(id) -> Forum</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing forum.

Required permissions:
 - `forum:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.forums().retrieve(
    "id",
    RetrieveForumsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the forum or experience to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.forums.update(id, request) -> Forum</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Update moderation and notification settings for a forum, such as who can post, who can comment, and email notification preferences.

Required permissions:
 - `forum:moderate`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.forums().update(
    "id",
    UpdateForumsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the forum to update. Accepts either an experience ID (e.g. 'exp_xxxxx') or a forum ID.
    
</dd>
</dl>

<dl>
<dd>

**bannedWords:** `Optional<List<String>>` — A list of words that are automatically blocked from posts in this forum. For example, ['spam', 'scam'].
    
</dd>
</dl>

<dl>
<dd>

**bannerImage:** `Optional<UpdateForumsRequestBannerImage>` — The banner image displayed at the top of the forum page. Pass null to remove the existing banner.
    
</dd>
</dl>

<dl>
<dd>

**emailNotificationPreference:** `Optional<ForumEmailNotificationPreferences>` — Controls how email notifications are sent to members when new posts are created in this forum.
    
</dd>
</dl>

<dl>
<dd>

**whoCanComment:** `Optional<ForumWhoCanCommentTypes>` — Controls which roles are allowed to comment on posts in this forum.
    
</dd>
</dl>

<dl>
<dd>

**whoCanPost:** `Optional<ForumWhoCanPostTypes>` — Controls which roles are allowed to create new posts in this forum.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## IdentityProfiles
<details><summary><code>client.identityProfiles.listIdentityProfile() -> SyncPagingIterable&amp;lt;IdentityProfileListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of identity profiles. When company_id is provided, lists IPs currently linked to that company's ledger. When omitted, lists IPs linked to any ledger the actor can read (including child companies under a parent).

Required permissions:
 - `identity:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.identityProfiles().listIdentityProfile(
    ListIdentityProfileRequest
        .builder()
        .first(42)
        .last(42)
        .companyId("biz_xxxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `Optional<String>` — The unique identifier of the company to filter to. When omitted, returns IPs across all ledgers the actor can read.
    
</dd>
</dl>

<dl>
<dd>

**profileType:** `Optional<IdentityProfileKinds>` 
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<IdentityProfileStatuses>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.identityProfiles.retrieveIdentityProfile(id) -> IdentityProfile</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing identity profile.

Required permissions:
 - `identity:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.identityProfiles().retrieveIdentityProfile(
    "idpf_xxxxxxxxxxxxx",
    RetrieveIdentityProfileRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the identity profile (idpf_xxx).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.identityProfiles.unlinkIdentityProfile(id) -> IdentityProfile</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Unlinks an IdentityProfile from a LedgerAccount (flips the matching link to is_current=false).
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.identityProfiles().unlinkIdentityProfile(
    "idpf_xxxxxxxxxxxxx",
    UnlinkIdentityProfileRequest
        .builder()
        .ledgerAccountId("ldgr_xxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ID of the IdentityProfile to unlink.
    
</dd>
</dl>

<dl>
<dd>

**ledgerAccountId:** `String` — The ID of the LedgerAccount to unlink the identity profile from.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.identityProfiles.listVerificationsIdentityProfile(id) -> SyncPagingIterable&amp;lt;ListVerificationsIdentityProfileResponseDataItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a list of verifications attached to an identity profile, ordered by most recent first.

Required permissions:
 - `identity:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.identityProfiles().listVerificationsIdentityProfile(
    "idpf_xxxxxxxxxxxxx",
    ListVerificationsIdentityProfileRequest
        .builder()
        .first(42)
        .last(42)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the identity profile (idpf_xxx).
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Invoices
<details><summary><code>client.invoices.list() -> SyncPagingIterable&amp;lt;InvoiceListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of invoices for a company, with optional filtering by product, status, collection method, and creation date.

Required permissions:
 - `invoice:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.invoices().list(
    ListInvoicesRequest
        .builder()
        .first(42)
        .last(42)
        .companyId("biz_xxxxxxxxxxxxxx")
        .createdBefore(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .createdAfter(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `Optional<String>` — The unique identifier of the company to list invoices for.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<Direction>` 
    
</dd>
</dl>

<dl>
<dd>

**productIds:** `Optional<String>` — Filter invoices to only those associated with these specific product identifiers.
    
</dd>
</dl>

<dl>
<dd>

**collectionMethods:** `Optional<InvoiceCollectionMethods>` — Filter invoices by their collection method.
    
</dd>
</dl>

<dl>
<dd>

**statuses:** `Optional<InvoiceStatuses>` — Filter invoices by their current status.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<InvoicesSortableColumns>` 
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<OffsetDateTime>` — Only return invoices created before this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<OffsetDateTime>` — Only return invoices created after this timestamp.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.invoices.create(request) -> Invoice</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Create an invoice for a customer. The invoice can be charged automatically using a stored payment method, or sent to the customer for manual payment.

Required permissions:
 - `invoice:create`
 - `member:email:read`
 - `member:basic:read`
 - `payment:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.invoices().create(
    CreateInvoicesRequest.of(
        CreateInvoicesRequestProduct
            .builder()
            .collectionMethod(InvoiceCollectionMethods.SEND_INVOICE)
            .companyId("biz_xxxxxxxxxxxxxx")
            .plan(
                CreateInvoicesRequestProductPlan
                    .builder()
                    .build()
            )
            .product(
                CreateInvoicesRequestProductProduct
                    .builder()
                    .title("title")
                    .build()
            )
            .build()
    )
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**request:** `CreateInvoicesRequest` — Parameters for CreateInvoice
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.invoices.retrieve(id) -> Invoice</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing invoice.

Required permissions:
 - `invoice:basic:read`
 - `member:email:read`
 - `member:basic:read`
 - `payment:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.invoices().retrieve(
    "inv_xxxxxxxxxxxxxx",
    RetrieveInvoicesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the invoice, or a secure token.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.invoices.delete(id) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Delete a draft invoice.

Required permissions:
 - `invoice:update`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.invoices().delete(
    "inv_xxxxxxxxxxxxxx",
    DeleteInvoicesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the draft invoice to delete.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.invoices.update(id, request) -> Invoice</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Update a draft invoice's details.

Required permissions:
 - `invoice:update`
 - `member:email:read`
 - `member:basic:read`
 - `payment:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.invoices().update(
    "inv_xxxxxxxxxxxxxx",
    UpdateInvoicesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the invoice to update.
    
</dd>
</dl>

<dl>
<dd>

**automaticallyFinalizesAt:** `Optional<OffsetDateTime>` — The date and time when the invoice will be automatically finalized. For charge_automatically, triggers an automatic charge. For send_invoice, sends the invoice email at the specified time.
    
</dd>
</dl>

<dl>
<dd>

**billingAddress:** `Optional<UpdateInvoicesRequestBillingAddress>` — Inline billing address to create or update a mailing address for this invoice.
    
</dd>
</dl>

<dl>
<dd>

**chargeBuyerFee:** `Optional<Boolean>` — Whether to charge the customer a buyer fee on this invoice.
    
</dd>
</dl>

<dl>
<dd>

**collectionMethod:** `Optional<InvoiceCollectionMethods>` — How the invoice should be collected.
    
</dd>
</dl>

<dl>
<dd>

**customerName:** `Optional<String>` — The name of the customer.
    
</dd>
</dl>

<dl>
<dd>

**dueDate:** `Optional<OffsetDateTime>` — The date by which the invoice must be paid.
    
</dd>
</dl>

<dl>
<dd>

**emailAddress:** `Optional<String>` — The email address of the customer.
    
</dd>
</dl>

<dl>
<dd>

**lineItems:** `Optional<List<UpdateInvoicesRequestLineItemsItem>>` — Line items that break down the invoice total.
    
</dd>
</dl>

<dl>
<dd>

**mailingAddressId:** `Optional<String>` — The unique identifier of an existing mailing address to attach.
    
</dd>
</dl>

<dl>
<dd>

**memberId:** `Optional<String>` — The unique identifier of a member to assign as the customer.
    
</dd>
</dl>

<dl>
<dd>

**paymentMethodId:** `Optional<String>` — The unique identifier of the payment method to charge.
    
</dd>
</dl>

<dl>
<dd>

**plan:** `Optional<UpdateInvoicesRequestPlan>` — Updated plan attributes.
    
</dd>
</dl>

<dl>
<dd>

**productId:** `Optional<String>` — The unique identifier of an existing product to attach to this invoice. Only allowed while the invoice is still a draft.
    
</dd>
</dl>

<dl>
<dd>

**subscriptionBillingAnchorAt:** `Optional<OffsetDateTime>` — The date that defines when the subscription billing cycle should start.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.invoices.markPaid(id) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Mark an open invoice as paid when payment was collected outside of Whop.

Required permissions:
 - `invoice:update`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.invoices().markPaid(
    "inv_xxxxxxxxxxxxxx",
    MarkPaidInvoicesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the invoice to mark as paid.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.invoices.markUncollectible(id) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Mark an open invoice as uncollectible when payment is not expected.

Required permissions:
 - `invoice:update`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.invoices().markUncollectible(
    "inv_xxxxxxxxxxxxxx",
    MarkUncollectibleInvoicesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the invoice to mark as uncollectible.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.invoices.resend(id) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Resend the notification email for an existing invoice to the customer.

Required permissions:
 - `invoice:update`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.invoices().resend(
    "inv_xxxxxxxxxxxxxx",
    ResendInvoicesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the invoice to resend.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.invoices.void_(id) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Void an open invoice so it can no longer be paid. Voiding is permanent and cannot be undone.

Required permissions:
 - `invoice:update`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.invoices().void_(
    "inv_xxxxxxxxxxxxxx",
    VoidInvoicesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the invoice to void.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Leads
<details><summary><code>client.leads.list() -> SyncPagingIterable&amp;lt;LeadListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of leads for a company, with optional filtering by product and creation date.

Required permissions:
 - `lead:basic:read`
 - `member:email:read`
 - `access_pass:basic:read`
 - `member:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.leads().list(
    ListLeadsRequest
        .builder()
        .companyId("biz_xxxxxxxxxxxxxx")
        .first(42)
        .last(42)
        .createdAfter(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .createdBefore(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `String` — The unique identifier of the company to list leads for.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<OffsetDateTime>` — Only return leads created after this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<OffsetDateTime>` — Only return leads created before this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**productIds:** `Optional<String>` — Filter leads to only those associated with these specific product identifiers.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.leads.create(request) -> Lead</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Record a new lead for a company, capturing a potential customer's interest in a specific product.

Required permissions:
 - `lead:manage`
 - `member:email:read`
 - `access_pass:basic:read`
 - `member:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.leads().create(
    CreateLeadsRequest
        .builder()
        .companyId("biz_xxxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**companyId:** `String` — The unique identifier of the company to create the lead for, starting with 'biz_'.
    
</dd>
</dl>

<dl>
<dd>

**metadata:** `Optional<Map<String, Object>>` — A JSON object of custom metadata to attach to the lead for tracking purposes.
    
</dd>
</dl>

<dl>
<dd>

**productId:** `Optional<String>` — The unique identifier of the product the lead is interested in, starting with 'prod_'.
    
</dd>
</dl>

<dl>
<dd>

**referrer:** `Optional<String>` — The referral URL that brought the lead to the company, such as 'https://example.com/landing'.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — The unique identifier of the user to record as the lead. If authenticated as a user, that user is used automatically.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.leads.retrieve(id) -> Lead</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing lead.

Required permissions:
 - `lead:basic:read`
 - `member:email:read`
 - `access_pass:basic:read`
 - `member:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.leads().retrieve(
    "lead_xxxxxxxxxxxxx",
    RetrieveLeadsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the lead to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.leads.update(id, request) -> Lead</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Update the metadata or referrer information on an existing lead record.

Required permissions:
 - `lead:manage`
 - `member:email:read`
 - `access_pass:basic:read`
 - `member:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.leads().update(
    "lead_xxxxxxxxxxxxx",
    UpdateLeadsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the lead to update, starting with 'lead_'.
    
</dd>
</dl>

<dl>
<dd>

**metadata:** `Optional<Map<String, Object>>` — A JSON object of custom metadata to set on the lead, replacing any existing metadata.
    
</dd>
</dl>

<dl>
<dd>

**referrer:** `Optional<String>` — The updated referral URL for the lead, such as 'https://example.com/landing'.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## LedgerAccounts
<details><summary><code>client.ledgerAccounts.retrieve(id) -> LedgerAccount</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing ledger account.

Required permissions:
 - `company:balance:read`
 - `payout:account:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.ledgerAccounts().retrieve(
    "ldgr_xxxxxxxxxxxxx",
    RetrieveLedgerAccountsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The identifier to look up the ledger account. Accepts a user ID ('user_xxx'), company ID ('biz_xxx'), or ledger account ID ('ldgr_xxx').
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Media
<details><summary><code>client.media.generate(request) -> MediaAsset</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Starts an AI media generation job billed from the account's balance. Generation is asynchronous — poll `GET /media/{id}` until the asset is `ready`, then use `file.id` anywhere attachments are accepted.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.media().generate(
    GenerateMediaRequest
        .builder()
        .prompt("A 9:16 product showcase of a cordless power scrubber")
        .type(GenerateMediaRequestType.VIDEO)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — Account ID, prefixed `biz_`. Defaults to the account the API key belongs to.
    
</dd>
</dl>

<dl>
<dd>

**durationSeconds:** `Optional<Integer>` — Video length in seconds. Video only; defaults to 5.
    
</dd>
</dl>

<dl>
<dd>

**prompt:** `String` — What to generate. Up to 2,000 characters.
    
</dd>
</dl>

<dl>
<dd>

**referenceMedia:** `Optional<List<String>>` — Optional reference image file IDs (`file_` prefixed), up to 4. For video, a single reference seeds the opening frame; multiple references guide subject and style instead.
    
</dd>
</dl>

<dl>
<dd>

**resolution:** `Optional<GenerateMediaRequestResolution>` — Video resolution. Video only; defaults to `1080p`. `1080p` is not supported by Seedance 2.0 Fast or Mini; `4k` is only supported by Seedance 2.0.
    
</dd>
</dl>

<dl>
<dd>

**type:** `GenerateMediaRequestType` — The kind of media to generate.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.media.retrieve(id) -> MediaAsset</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a media asset by ID. Poll this while the asset is `processing`.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.media().retrieve(
    "id",
    RetrieveMediaRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Media asset ID, prefixed `media_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Members
<details><summary><code>client.members.list() -> SyncPagingIterable&amp;lt;Member&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the members of an account. A member is one buyer's relationship with the account, regardless of how many memberships they hold.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.members().list(
    ListMembersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The account to list members for (`biz_` tag). Defaults to the account the credential acts as.
    
</dd>
</dl>

<dl>
<dd>

**accessLevel:** `Optional<ListMembersRequestAccessLevel>` — Filter by what the member can reach on the account.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<ListMembersRequestStatus>` — Filter by whether the member is still part of the account.
    
</dd>
</dl>

<dl>
<dd>

**query:** `Optional<String>` — Search members by name or username. An exact email address also matches when the credential holds the member:email:read scope.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Only members who joined after this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Only members who joined before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListMembersRequestOrder>` — Sort field.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListMembersRequestDirection>` — Sort direction.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Number of members to return from the start of the window.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor to paginate forwards from.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Number of members to return from the end of the window.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Cursor to paginate backwards from.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.members.retrieve(id) -> Member</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a member by ID. Accessible to the account and to the member's own user.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.members().retrieve(
    "id",
    RetrieveMembersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Member ID (`mber_` tag).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Memberships
<details><summary><code>client.memberships.list() -> SyncPagingIterable&amp;lt;Membership&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists every membership the caller can read: an account API key its account's; a user credential their own plus those of every account they manage. `account_id` and `user_id` only narrow that list — values outside the caller's reach return fewer results, not an error.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.memberships().list(
    ListMembershipsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — Narrow to one account (`biz_` tag). With read access to the account this lists all of its memberships; without, only the caller's own memberships in it.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — Narrow to one user's memberships (`user_` tag, or `me` for the caller). A user outside the caller's visible set returns an empty list.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<ListMembershipsRequestStatus>` — Filter by billing state. `canceling` matches active memberships set to cancel at period end; `paused` matches memberships with payment collection paused.
    
</dd>
</dl>

<dl>
<dd>

**productId:** `Optional<String>` — Filter to memberships of this product (`prod_` tag). Repeat as product_ids[] for several.
    
</dd>
</dl>

<dl>
<dd>

**planId:** `Optional<String>` — Filter to memberships of this plan (`plan_` tag). Repeat as plan_ids[] for several.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Only memberships created after this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Only memberships created before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListMembershipsRequestOrder>` — Sort field.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListMembershipsRequestDirection>` — Sort direction.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Number of memberships to return from the start of the window.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor to paginate forwards from.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Number of memberships to return from the end of the window.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Cursor to paginate backwards from.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.memberships.invite(request) -> InviteMembershipsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Sends an email inviting one recipient to join the account through a free plan. Identify the recipient by exactly one of `user_id` or `email`. The invitation is bound to that recipient; after signing in, accepting it immediately grants the membership without checkout. This Experimental endpoint is available only to accounts enabled for membership invitations.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.memberships().invite(
    InviteMembershipsRequestBody.of(
        InviteMembershipsRequestBodyUserId
            .builder()
            .planId("plan_xxxxxxxxxxxxxx")
            .userId("user_xxxxxxxxxxxxxx")
            .build()
    )
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**request:** `InviteMembershipsRequestBody` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.memberships.retrieve(id) -> Membership</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a membership by ID or license key. Accessible to the account and to the membership's own user.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.memberships().retrieve(
    "id",
    RetrieveMembershipsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Membership ID (`mem_` tag), or a software license key.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.memberships.update(id, request) -> Membership</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Updates a membership: merge metadata key-value pairs, or toggle `cancel_at_period_end` — `true` schedules the cancellation for the end of the current billing period, `false` reverses a pending one.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.memberships().update(
    "id",
    UpdateMembershipsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Membership ID (`mem_` tag), or a software license key.
    
</dd>
</dl>

<dl>
<dd>

**cancelAtPeriodEnd:** `Optional<Boolean>` — `true` cancels at the end of the current billing period (the customer keeps access until then); `false` reverses a pending cancellation.
    
</dd>
</dl>

<dl>
<dd>

**metadata:** `Optional<Map<String, Object>>` — Key-value pairs to merge into the membership's metadata. Pass an empty object to clear it.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.memberships.addFreeDaysMembership(id, request) -> Membership</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Add free days to extend a membership's current billing period, expiration date, or Stripe trial.

Required permissions:
 - `member:manage`
 - `member:email:read`
 - `member:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.memberships().addFreeDaysMembership(
    "mem_xxxxxxxxxxxxxx",
    AddFreeDaysMembershipRequest
        .builder()
        .freeDays(42)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the membership.
    
</dd>
</dl>

<dl>
<dd>

**freeDays:** `Integer` — The number of free days to add (1-1095). Extends the billing period, expiration date, or Stripe trial depending on plan type.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.memberships.cancel(id, request) -> Membership</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Cancels a membership. Pass `cancel_at_period_end: true` to stop auto-renewal and keep access until the current billing period ends. Omit it (or pass `false`) to revoke access immediately. Buyers cannot cancel buy-now-pay-later (`splitit`, `sezzle`) or non-trial split-pay memberships.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.memberships().cancel(
    "id",
    CancelMembershipsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Membership ID (`mem_` tag).
    
</dd>
</dl>

<dl>
<dd>

**cancelAtPeriodEnd:** `Optional<Boolean>` — `true` stops auto-renewal and keeps access until the current billing period ends. Omit or `false` revokes access immediately.
    
</dd>
</dl>

<dl>
<dd>

**reason:** `Optional<String>` — Free-form note recording why the membership was canceled.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.memberships.extend(id, request) -> Membership</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Adds free days to a membership, extending its current billing period, expiration date, or trial depending on the plan type.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.memberships().extend(
    "id",
    ExtendMembershipsRequest
        .builder()
        .days(7)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Membership ID (`mem_` tag).
    
</dd>
</dl>

<dl>
<dd>

**days:** `Integer` — Number of free days to add (1-1095).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.memberships.pause(id, request) -> Membership</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Pauses a membership's recurring payment collection. The customer keeps access but is not charged until the membership is resumed.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.memberships().pause(
    "id",
    PauseMembershipsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Membership ID (`mem_` tag).
    
</dd>
</dl>

<dl>
<dd>

**until:** `Optional<String>` — ISO 8601 time to automatically resume payment collection. Must be in the future; only supported for memberships billed by Whop.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.memberships.resume(id) -> Membership</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Resumes a previously paused membership's recurring payment collection. Billing resumes on the next cycle.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.memberships().resume(
    "id",
    ResumeMembershipsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Membership ID (`mem_` tag).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.memberships.resyncAccessMembership(id) -> Membership</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Re-run access fulfillment for a membership. Recomputes the member's content access on Whop, re-validates their Discord link (re-adding them to the server and re-assigning roles if needed), and re-fulfills TradingView indicator access. Telegram access is invite-based and cannot be resynced here. The outcome is written to the membership's logs.

Required permissions:
 - `membership:resync_access`
 - `member:email:read`
 - `member:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.memberships().resyncAccessMembership(
    "mem_xxxxxxxxxxxxxx",
    ResyncAccessMembershipRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the membership to resync access for.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.memberships.transfer(id) -> TransferMembershipsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Creates a one-use transfer URL for a membership. Opening the URL while logged into a different Whop account claims the membership onto that account. The membership's buyer can generate a link for their own membership with `membership:transfer` when the product allows transfers and the membership is `trialing`, `active`, or `completed`. An account credential with `membership:update` bypasses both restrictions.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.memberships().transfer(
    "id",
    TransferMembershipsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Membership ID (`mem_` tag).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.memberships.uncancelMembership(id) -> Membership</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Reverse a pending cancellation for a membership that was scheduled to cancel at period end.

Required permissions:
 - `member:manage`
 - `member:email:read`
 - `member:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.memberships().uncancelMembership(
    "mem_xxxxxxxxxxxxxx",
    UncancelMembershipRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the membership to uncancel.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Messages
<details><summary><code>client.messages.list() -> SyncPagingIterable&amp;lt;MessageListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of messages within a specific experience chat, DM, or group chat channel, sorted by creation time.

Required permissions (one of):
 - `chat:read`
 - `dms:read`
 - `support_chat:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.messages().list(
    ListMessagesRequest
        .builder()
        .channelId("channel_id")
        .first(42)
        .last(42)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**channelId:** `String` — The unique identifier of the channel or experience to list messages for.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<Direction>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.messages.create(request) -> Message</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Send a new message in an experience chat, DM, or group chat channel. Supports text content, attachments, polls, and replies.

Required permissions (one of):
 - `chat:message:create`
 - `dms:message:manage`
 - `livestream:chat:write`
 - `support_chat:message:create`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.messages().create(
    CreateMessagesRequest
        .builder()
        .channelId("channel_id")
        .content("content")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**attachments:** `Optional<List<CreateMessagesRequestAttachmentsItem>>` — A list of file attachments to include with the message, such as images or videos.
    
</dd>
</dl>

<dl>
<dd>

**autoDetectLinks:** `Optional<Boolean>` — Automatically detect URLs in the message and generate link previews.
    
</dd>
</dl>

<dl>
<dd>

**channelId:** `String` — The unique identifier of the channel or experience to send the message in. For example, 'exp_xxxxx' or 'feed_xxxxx'.
    
</dd>
</dl>

<dl>
<dd>

**content:** `String` — The body of the message in Markdown format. For example, 'Hello **world**'.
    
</dd>
</dl>

<dl>
<dd>

**poll:** `Optional<CreateMessagesRequestPoll>` — A poll to attach to this message, allowing recipients to vote on options.
    
</dd>
</dl>

<dl>
<dd>

**replyingToMessageId:** `Optional<String>` — The unique identifier of the message this is replying to, creating a threaded reply.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.messages.retrieve(id) -> Message</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing message.

Required permissions (one of):
 - `chat:read`
 - `dms:read`
 - `livestream:chat:read`
 - `support_chat:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.messages().retrieve(
    "id",
    RetrieveMessagesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the message to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.messages.delete(id) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Permanently delete a message from an experience chat, DM, or group chat channel. Only the message author or a channel admin can delete a message.

Required permissions (one of):
 - `chat:message:create` and `chat:read`
 - `dms:message:manage` and `dms:read`
 - `livestream:chat:write` and `livestream:chat:read`
 - `support_chat:message:create` and `support_chat:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.messages().delete(
    "id",
    DeleteMessagesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the message to delete.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.messages.update(id, request) -> Message</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Edit the content, attachments, or pinned status of an existing message in an experience chat, DM, or group chat channel.

Required permissions (one of):
 - `chat:message:create`
 - `dms:message:manage`
 - `livestream:chat:write`
 - `support_chat:message:create`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.messages().update(
    "id",
    UpdateMessagesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the message to update.
    
</dd>
</dl>

<dl>
<dd>

**attachments:** `Optional<List<UpdateMessagesRequestAttachmentsItem>>` — A replacement list of file attachments for this message, such as images or videos.
    
</dd>
</dl>

<dl>
<dd>

**content:** `Optional<String>` — The updated body of the message in Markdown format. For example, 'Hello **world**'.
    
</dd>
</dl>

<dl>
<dd>

**isPinned:** `Optional<Boolean>` — Whether this message should be pinned to the top of the channel.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Notifications
<details><summary><code>client.notifications.list() -> SyncPagingIterable&amp;lt;Notification&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the authenticated user's notifications, newest first. Requires a user credential — an account API key has no notification feed. Without filters the feed spans every experience the user belongs to plus the teams they are a member of.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.notifications().list(
    ListNotificationsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**unread:** `Optional<Boolean>` — Only return notifications created since the user last viewed their source.
    
</dd>
</dl>

<dl>
<dd>

**experienceId:** `Optional<String>` — Only return notifications from this experience (`exp_` tag).
    
</dd>
</dl>

<dl>
<dd>

**accountId:** `Optional<String>` — Only return team notifications for this account (`biz_` tag).
    
</dd>
</dl>

<dl>
<dd>

**mentions:** `Optional<Boolean>` — Only return notifications that mention the user directly.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of notifications to return (default 20, max 100).
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor (a notification `id` from a previous page); returns notifications older than it.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.notifications.create(request) -> CreateNotificationsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Queues a notification to every user of an experience or to an account's team, processed asynchronously. Every send is attributed to an app: use an app API key, or a credential acting on behalf of an app. Narrow the audience with `user_ids` to send a mention.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.notifications().create(
    CreateNotificationsRequest
        .builder()
        .content("Drop off at 4180 Burnet Rd. Plan on two days for the full coating.")
        .title("Your ceramic coating is booked")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — Account whose team members receive the notification (`biz_` tag). Exactly one of `experience_id` or `account_id` is required.
    
</dd>
</dl>

<dl>
<dd>

**content:** `String` — Main body text of the notification.
    
</dd>
</dl>

<dl>
<dd>

**experienceId:** `Optional<String>` — Experience whose users receive the notification (`exp_` tag). Exactly one of `experience_id` or `account_id` is required.
    
</dd>
</dl>

<dl>
<dd>

**iconUserId:** `Optional<String>` — User whose profile picture is used as the notification icon. Defaults to the experience or account avatar.
    
</dd>
</dl>

<dl>
<dd>

**restPath:** `Optional<String>` — Path segment appended to the generated deep link that opens your app, for example `/settings/billing`.
    
</dd>
</dl>

<dl>
<dd>

**subtitle:** `Optional<String>` — Optional secondary line displayed below the title.
    
</dd>
</dl>

<dl>
<dd>

**title:** `String` — Headline text of the notification.
    
</dd>
</dl>

<dl>
<dd>

**userIds:** `Optional<List<String>>` — Optional `user_` tags narrowing the audience. When provided, only these users are notified (as a mention), provided they are in the targeted experience or account.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.notifications.badges() -> BadgesNotificationsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the authenticated user's per-experience unread badge state. Requires a user credential. Returns one row per experience the user belongs to (or per requested experience).
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.notifications().badges(
    BadgesNotificationsRequest
        .builder()
        .experienceIds(
            Arrays.asList("exp_xxxxxxxxxxxxxx")
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**experienceIds:** `Optional<String>` — Only return badges for these experiences (`exp_` tags).
    
</dd>
</dl>

<dl>
<dd>

**lastFetchedAt:** `Optional<String>` — The client's last fetched-at ISO 8601 timestamp, used to partially refresh badges after a websocket message.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.notifications.markRead(request) -> MarkReadNotificationsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Marks the authenticated user's notifications as read: one experience's (`experience_id`) or everything (`all: true`) — exactly one of the two. Requires a user credential. Responds with the refreshed badge rows for the affected scope.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.notifications().markRead(
    MarkReadNotificationsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**all:** `Optional<Boolean>` — Pass `true` to mark every notification read. Exactly one of `experience_id` or `all` is required.
    
</dd>
</dl>

<dl>
<dd>

**experienceId:** `Optional<String>` — Experience to mark read (`exp_` tag). Exactly one of `experience_id` or `all` is required.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.notifications.retrieve(id) -> Notification</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a single notification by id — either an `id` returned by List Notifications, or the ephemeral id delivered with a push/websocket event. Requires a user credential.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.notifications().retrieve(
    "id",
    RetrieveNotificationsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — A notification `id` from List Notifications, or the id delivered with a push/websocket event.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Partners
<details><summary><code>client.partners.create() -> CreatePartnersResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Enrolls the calling user in the Whop partner program, making their partner businesses eligible for earnings. Idempotent — enrolling again keeps the original enrollment time.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.partners().create();
```
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.partners.leaderboard() -> LeaderboardPartnersResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Ranks referrers by partner business earnings — all-time by default, or over the current day, month, year, or trailing 30 days. Authentication is optional: authenticated callers also get their own standing, anonymous callers get the rankings alone.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.partners().leaderboard(
    LeaderboardPartnersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**period:** `Optional<LeaderboardPartnersRequestPeriod>` — Time window for the rankings. `day`, `month`, and `year` count earnings since the start of the current calendar day, month, or year; `last_30_days` counts earnings over the trailing 30 days; `all_time` ranks lifetime earnings.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.partners.referredUsers() -> SyncPagingIterable&amp;lt;ReferredUsersPartnersResponseDataItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the users the caller referred onto Whop (newest first), each with the second-tier earnings the caller has made from that user's businesses.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.partners().referredUsers(
    ReferredUsersPartnersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**hasBusinesses:** `Optional<Boolean>` — When true, only referred users who brought at least one business onto Whop.
    
</dd>
</dl>

<dl>
<dd>

**hasEarningBusinesses:** `Optional<Boolean>` — When true, only referred users with at least one business that has generated earnings.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Number of referred users to return from the start of the window.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor to fetch the page after (from page_info.end_cursor).
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Number of referred users to return from the end of the window.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Cursor to fetch the page before (from page_info.start_cursor).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Payment Method Domains
<details><summary><code>client.paymentMethodDomains.list() -> SyncPagingIterable&amp;lt;PaymentMethodDomain&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists payment method domains. Without `account_id`, returns the caller's own domains and those of every connected account.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.paymentMethodDomains().list(
    ListPaymentMethodDomainsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — Only domains registered for this account (`biz_` tag). Defaults to the caller's account plus its connected accounts.
    
</dd>
</dl>

<dl>
<dd>

**hostname:** `Optional<String>` — Only the domain with this exact hostname.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<ListPaymentMethodDomainsRequestStatus>` — Only domains with this verification status.
    
</dd>
</dl>

<dl>
<dd>

**provider:** `Optional<ListPaymentMethodDomainsRequestProvider>` — Only domains registered with this wallet provider.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<OffsetDateTime>` — Only domains created before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<OffsetDateTime>` — Only domains created after this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListPaymentMethodDomainsRequestOrder>` — Sort field.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListPaymentMethodDomainsRequestDirection>` — Sort direction.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Number of domains to return from the start of the window.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor to paginate forwards from.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Number of domains to return from the end of the window.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Cursor to paginate backwards from.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.paymentMethodDomains.create(request) -> PaymentMethodDomain</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Registers a hostname with the wallet provider and attempts verification inline. Returns `verified` when the provider fetched the domain-association file (for Apple Pay, `/.well-known/apple-developer-merchantid-domain-association`), or `pending` when it could not — host the file, then retry with the verify endpoint.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.paymentMethodDomains().create(
    CreatePaymentMethodDomainsRequest
        .builder()
        .hostname("pending.shinetime.example")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — Account to register the domain for (`biz_` tag). Defaults to the caller's account.
    
</dd>
</dl>

<dl>
<dd>

**hostname:** `String` — Hostname to register (e.g. `checkout.shinetime.example`).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.paymentMethodDomains.retrieve(id) -> PaymentMethodDomain</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a payment method domain to check its verification status.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.paymentMethodDomains().retrieve(
    "id",
    RetrievePaymentMethodDomainsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the payment method domain, prefixed `pmd_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.paymentMethodDomains.delete(id) -> DeletePaymentMethodDomainsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Unregisters a payment method domain so its wallet payment methods stop rendering there.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.paymentMethodDomains().delete(
    "id",
    DeletePaymentMethodDomainsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the payment method domain, prefixed `pmd_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.paymentMethodDomains.verify(id) -> PaymentMethodDomain</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Re-attempts provider verification of a pending domain once the association file is hosted. Fails with a `bad_request` explaining what to fix; verifying an already `verified` domain is a no-op.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.paymentMethodDomains().verify(
    "id",
    VerifyPaymentMethodDomainsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the payment method domain, prefixed `pmd_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## PaymentMethods
<details><summary><code>client.paymentMethods.list() -> SyncPagingIterable&amp;lt;PaymentMethodListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of payment methods for a member or company, or for the authenticated user when neither is given, with optional filtering by creation date. A payment method is a stored representation of how a customer intends to pay, such as a card, bank account, or digital wallet.

Required permissions:
 - `member:payment_methods:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.paymentMethods().list(
    ListPaymentMethodsRequest
        .builder()
        .first(42)
        .last(42)
        .memberId("mber_xxxxxxxxxxxxx")
        .companyId("biz_xxxxxxxxxxxxxx")
        .createdBefore(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .createdAfter(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**memberId:** `Optional<String>` — The unique identifier of the member to list payment methods for. Omit this and company_id to list your own saved payment methods.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `Optional<String>` — The unique identifier of the company. Provide either this or member_id, not both. Omit both to address your own saved payment methods.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<Direction>` 
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<OffsetDateTime>` — Only return payment methods created before this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<OffsetDateTime>` — Only return payment methods created after this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**futureUsage:** `Optional<FutureUsageTypes>` 
    
</dd>
</dl>

<dl>
<dd>

**paymentMethodTypes:** `Optional<PaymentMethodTypes>` — Only return payment methods of these types. Pass the eligible `type` values from the payment method types catalogue so the list holds nothing the purchase cannot take. An empty list returns no payment methods.
    
</dd>
</dl>

<dl>
<dd>

**cardBrands:** `Optional<CardBrands>` — Only return cards on these networks, such as the networks the seller accepts. Payment methods that are not cards are unaffected.
    
</dd>
</dl>

<dl>
<dd>

**cardFundingTypes:** `Optional<CardFundingTypes>` — Only return cards funded this way. A card whose funding could not be determined is excluded, and payment methods that are not cards are unaffected.
    
</dd>
</dl>

<dl>
<dd>

**hasPayerDocument:** `Optional<Boolean>` — Filter cards by whether they carry the payer identity document their payment provider requires. Payment methods that are not cards are unaffected.
    
</dd>
</dl>

<dl>
<dd>

**expired:** `Optional<Boolean>` — Filter by expiry. Only a card can expire, so `false` keeps every payment method that is not past its expiration month and `true` returns expired cards alone.
    
</dd>
</dl>

<dl>
<dd>

**broken:** `Optional<Boolean>` — Filter by whether the stored credential has permanently stopped charging, such as a vault entry its provider closed.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.paymentMethods.retrieve(id) -> PaymentMethod</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing payment method. Addresses a member's wallet when member_id or company_id is given, otherwise your own.

Required permissions:
 - `member:payment_methods:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.paymentMethods().retrieve(
    "payt_xxxxxxxxxxxxx",
    RetrievePaymentMethodsRequest
        .builder()
        .companyId("biz_xxxxxxxxxxxxxx")
        .memberId("mber_xxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the payment method.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `Optional<String>` — The unique identifier of the company. Provide either this or member_id, not both. Omit both to address your own saved payment methods.
    
</dd>
</dl>

<dl>
<dd>

**memberId:** `Optional<String>` — The unique identifier of the member. Provide either this or company_id, not both. Omit both to address your own saved payment methods.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.paymentMethods.deletePaymentMethod(id) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Delete a saved payment method. Cannot delete a payment method attached to an active subscription.

Required permissions:
 - `member:payment_methods:manage`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.paymentMethods().deletePaymentMethod(
    "payt_xxxxxxxxxxxxx",
    DeletePaymentMethodRequest
        .builder()
        .companyId("biz_xxxxxxxxxxxxxx")
        .memberId("mber_xxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the payment method to delete.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `Optional<String>` — The unique identifier of the company. Provide either this or member_id, not both. Omit both to address your own saved payment methods.
    
</dd>
</dl>

<dl>
<dd>

**memberId:** `Optional<String>` — The unique identifier of the member. Provide either this or company_id, not both. Omit both to address your own saved payment methods.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Payments
<details><summary><code>client.payments.list() -> SyncPagingIterable&amp;lt;PaymentListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of payments for the actor in context, with optional filtering by product, plan, status, billing reason, currency, and creation date.

Required permissions:
 - `payment:basic:read`
 - `plan:basic:read`
 - `access_pass:basic:read`
 - `member:email:read`
 - `member:basic:read`
 - `member:phone:read`
 - `promo_code:basic:read`
 - `shipment:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.payments().list(
    ListPaymentsRequest
        .builder()
        .first(42)
        .last(42)
        .companyId("biz_xxxxxxxxxxxxxx")
        .createdBefore(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .createdAfter(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .updatedBefore(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .updatedAfter(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `Optional<String>` — The unique identifier of the company to list payments for.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<Direction>` 
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ReceiptV2Order>` 
    
</dd>
</dl>

<dl>
<dd>

**productIds:** `Optional<String>` — Filter payments to only those associated with these specific product identifiers.
    
</dd>
</dl>

<dl>
<dd>

**billingReasons:** `Optional<BillingReasons>` — Filter payments by their billing reason.
    
</dd>
</dl>

<dl>
<dd>

**currencies:** `Optional<Currencies>` — Filter payments by their currency code.
    
</dd>
</dl>

<dl>
<dd>

**planIds:** `Optional<String>` — Filter payments to only those associated with these specific plan identifiers.
    
</dd>
</dl>

<dl>
<dd>

**statuses:** `Optional<ReceiptStatus>` — Filter payments by their current status.
    
</dd>
</dl>

<dl>
<dd>

**substatuses:** `Optional<FriendlyReceiptStatus>` — Filter payments by their current substatus for more granular filtering.
    
</dd>
</dl>

<dl>
<dd>

**includeFree:** `Optional<Boolean>` — Whether to include payments with a zero amount.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<OffsetDateTime>` — Only return payments created before this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<OffsetDateTime>` — Only return payments created after this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**updatedBefore:** `Optional<OffsetDateTime>` — Only return payments last updated before this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**updatedAfter:** `Optional<OffsetDateTime>` — Only return payments last updated after this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**query:** `Optional<String>` — Search payments by user ID, membership ID, user email, name, or username. Email filtering requires the member:email:read permission.
    
</dd>
</dl>

<dl>
<dd>

**checkoutConfigurationIds:** `Optional<String>` — Only return payments from these checkout configurations.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.payments.create(request) -> CreatePaymentsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Charge an existing member off-session using one of their stored payment methods. You can provide an existing plan, or create a new one in-line. This endpoint will respond with a payment object immediately, but the payment is processed asynchronously in the background. Use webhooks to be notified when the payment succeeds or fails.

Required permissions:
 - `payment:charge`
 - `plan:create`
 - `access_pass:create`
 - `access_pass:update`
 - `plan:basic:read`
 - `access_pass:basic:read`
 - `member:email:read`
 - `member:basic:read`
 - `member:phone:read`
 - `promo_code:basic:read`
 - `shipment:basic:read`
 - `payment:dispute:read`
 - `payment:resolution_center_case:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.payments().create(
    CreatePaymentsRequest.of(
        CreatePaymentsRequestZero
            .builder()
            .companyId("biz_xxxxxxxxxxxxxx")
            .confirmationToken("confirmation_token")
            .plan(
                CreatePaymentsRequestZeroPlan
                    .builder()
                    .currency(Currencies.USD)
                    .build()
            )
            .build()
    )
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**request:** `CreatePaymentsRequest` — Parameters for CreatePayment
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.payments.retrieve(id) -> RetrievePaymentsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing payment.

Required permissions:
 - `payment:basic:read`
 - `plan:basic:read`
 - `access_pass:basic:read`
 - `member:email:read`
 - `member:basic:read`
 - `member:phone:read`
 - `promo_code:basic:read`
 - `shipment:basic:read`
 - `payment:dispute:read`
 - `payment:resolution_center_case:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.payments().retrieve(
    "pay_xxxxxxxxxxxxxx",
    RetrievePaymentsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the payment.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.payments.listFees(id) -> SyncPagingIterable&amp;lt;ListFeesPaymentsResponseDataItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns the list of fees associated with a specific payment, including platform fees and processing fees.

Required permissions:
 - `payment:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.payments().listFees(
    "pay_xxxxxxxxxxxxxx",
    ListFeesPaymentsRequest
        .builder()
        .first(42)
        .last(42)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the payment to list fees for.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.payments.refund(id, request) -> Payment</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Issue a full or partial refund for a payment. The refund is processed through the original payment processor and the membership status is updated accordingly.

Required permissions:
 - `payment:manage`
 - `plan:basic:read`
 - `access_pass:basic:read`
 - `member:email:read`
 - `member:basic:read`
 - `member:phone:read`
 - `promo_code:basic:read`
 - `shipment:basic:read`
 - `payment:dispute:read`
 - `payment:resolution_center_case:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.payments().refund(
    "pay_xxxxxxxxxxxxxx",
    RefundPaymentsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the payment to refund.
    
</dd>
</dl>

<dl>
<dd>

**partialAmount:** `Optional<Double>` — The amount to refund. For multi-currency payments, this is in the charge currency (what the buyer paid). For single-currency, this is in the payment currency. If omitted, the full payment amount is refunded.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.payments.retry(id) -> Payment</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retry a failed or pending payment. This re-attempts the charge using the original payment method and plan details.

Required permissions:
 - `payment:manage`
 - `plan:basic:read`
 - `access_pass:basic:read`
 - `member:email:read`
 - `member:basic:read`
 - `member:phone:read`
 - `promo_code:basic:read`
 - `shipment:basic:read`
 - `payment:dispute:read`
 - `payment:resolution_center_case:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.payments().retry(
    "pay_xxxxxxxxxxxxxx",
    RetryPaymentsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the payment to retry.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.payments.void_(id) -> Payment</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Void a payment that has not yet been settled. Voiding cancels the payment before it is captured by the payment processor.

Required permissions:
 - `payment:manage`
 - `plan:basic:read`
 - `access_pass:basic:read`
 - `member:email:read`
 - `member:basic:read`
 - `member:phone:read`
 - `promo_code:basic:read`
 - `shipment:basic:read`
 - `payment:dispute:read`
 - `payment:resolution_center_case:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.payments().void_(
    "pay_xxxxxxxxxxxxxx",
    VoidPaymentsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the payment to void.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.payments.updateReturnUrl(paymentId, request) -> PaymentStatus</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Changes where the buyer lands after completing an off-site step, up until they return. Accepts either a secret key or the payment's own `client_secret`, so the surface that knows the final destination can set it.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.payments().updateReturnUrl(
    "payment_id",
    UpdateReturnUrlPaymentsRequest
        .builder()
        .returnUrl("https://shinetime.example/checkout/thanks")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**paymentId:** `String` — The unique identifier of the payment.
    
</dd>
</dl>

<dl>
<dd>

**returnUrl:** `String` — Where the buyer continues after completing an off-site step. Must be an absolute https URL without credentials (http is allowed for localhost), at most 2,048 characters.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.payments.retrieveStatus(paymentId) -> PaymentStatus</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves how far a payment has got and what the buyer must do next, if anything. A payment is collected in the background, so poll this rather than reading the create response. Accepts either a secret key or the payment's own `client_secret`, so the surface collecting the payment can poll it directly.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.payments().retrieveStatus(
    "payment_id",
    RetrieveStatusPaymentsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**paymentId:** `String` — The unique identifier of the payment.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## PayoutAccounts
<details><summary><code>client.payoutAccounts.retrieve(id) -> PayoutAccount</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing payout account.

Required permissions:
 - `payout:account:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.payoutAccounts().retrieve(
    "poact_xxxxxxxxxxxx",
    RetrievePayoutAccountsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the payout account to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## PayoutMethods
<details><summary><code>client.payoutMethods.listPayoutMethod() -> SyncPagingIterable&amp;lt;PayoutMethodListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a list of active payout methods configured for a company, ordered by most recently created.

Required permissions:
 - `payout:destination:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.payoutMethods().listPayoutMethod(
    ListPayoutMethodRequest
        .builder()
        .companyId("biz_xxxxxxxxxxxxxx")
        .first(42)
        .last(42)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `String` — The unique identifier of the company to list payout methods for.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.payoutMethods.retrievePayoutMethod(id) -> PayoutMethod</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing payout method.

Required permissions:
 - `payout:destination:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.payoutMethods().retrievePayoutMethod(
    "potk_xxxxxxxxxxxxx",
    RetrievePayoutMethodRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the payout method to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Payouts
<details><summary><code>client.payouts.list() -> SyncPagingIterable&amp;lt;ListPayoutsResponseDataItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists an account's or user's payouts, newest first.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.payouts().list(
    ListPayoutsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The owning account ID (a biz_ identifier). Provide this or user_id.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — The owning user ID (a user_ identifier). Provide this or account_id.
    
</dd>
</dl>

<dl>
<dd>

**currency:** `Optional<String>` — Optional currency code filter, for example `usd`.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<ListPayoutsRequestStatus>` — Filter to payouts whose `status` reads this word, matching exactly what this version displays — `reversed` finds settled payouts the bank later returned. Requires Api-Version-Date 2026-08-21 or later.
    
</dd>
</dl>

<dl>
<dd>

**source:** `Optional<ListPayoutsRequestSource>` — Filter by how the payout was created. Payouts created before source tracking or through internal tooling carry no source and never match.
    
</dd>
</dl>

<dl>
<dd>

**payoutMethodId:** `Optional<String>` — Filter to payouts sent to one saved payout method (a pytk_ identifier). An unknown id matches nothing.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<OffsetDateTime>` — Only payouts created before this ISO 8601 time (exclusive).
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<OffsetDateTime>` — Only payouts created at or after this ISO 8601 time (inclusive).
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Number of payouts to return from the start of the window.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor to fetch the page after (from page_info.end_cursor).
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Number of payouts to return from the end of the window.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Cursor to fetch the page before (from page_info.start_cursor).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.payouts.create(request) -> CreatePayoutsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Sends money from an account or user balance to a saved payout method for that owner.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.payouts().create(
    CreatePayoutsRequestBody.of(new 
    HashMap<String, Object>() {{put("key", "value");
    }})
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**request:** `CreatePayoutsRequestBody` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.payouts.retrieve(id) -> RetrievePayoutsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Fetches one payout by its `wdrl_` ID, or by the `cofr_` conversion request ID a stablecoin payout carries as `payout_request_id` — both ids answer with the same payout object.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.payouts().retrieve(
    "id",
    RetrievePayoutsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Payout ID, prefixed `wdrl_` for a payout returned by `GET /payouts` or `cofr_` for the payout request returned by `POST /payouts`.
    
</dd>
</dl>

<dl>
<dd>

**accountId:** `Optional<String>` — Owning account ID, prefixed `biz_`. Provide exactly one of `account_id` or `user_id`.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — Owning user ID, prefixed `user_`. Provide exactly one of `account_id` or `user_id`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## People
<details><summary><code>client.people.list() -> SyncPagingIterable&amp;lt;ListPeopleResponseDataItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the people (visitors and customers) of an account: the identity-linked person profiles aggregated from every pixel, payment, and platform event — identities, purchases and LTV, geo/device profile, traffic sources, and first/last marketing touches.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.people().list(
    ListPeopleRequest
        .builder()
        .source(
            Arrays.asList("direct")
        )
        .eventName(
            Arrays.asList("payment.completed")
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — Account ID, prefixed `biz_`. Optional for account API keys; required for credentials that can access multiple accounts.
    
</dd>
</dl>

<dl>
<dd>

**query:** `Optional<String>` — Search people by name, email, phone, or whop user ID (case-insensitive substring match).
    
</dd>
</dl>

<dl>
<dd>

**source:** `Optional<String>` — Only include people acquired from any of these sources — canonical paths (whop:<campaign>:<group>:<ad>, ext:<platform>:..., referrer:<domain>, direct, other), exact or with a trailing :* prefix. The same vocabulary the events / people metrics use.
    
</dd>
</dl>

<dl>
<dd>

**attributionModel:** `Optional<ListPeopleRequestAttributionModel>` — Attribution model the source filter matches against (defaults to last_touch).
    
</dd>
</dl>

<dl>
<dd>

**eventName:** `Optional<String>` — Only include people who fired any of these events, e.g. payment.completed or page.checkout.view.
    
</dd>
</dl>

<dl>
<dd>

**customEvent:** `Optional<String>` — Only include people who fired this custom pixel event.
    
</dd>
</dl>

<dl>
<dd>

**eventFrom:** `Optional<OffsetDateTime>` — With event_to plus an event or source filter, switches to exact-population mode: person ids are resolved and paginated on the events side within this window (the same query the people metric counts), then hydrated per page.
    
</dd>
</dl>

<dl>
<dd>

**eventTo:** `Optional<OffsetDateTime>` — The inclusive end of the event window for exact-population mode.
    
</dd>
</dl>

<dl>
<dd>

**audienceId:** `Optional<String>` — Only include people in this audience. An audience that keeps itself up to date resolves to the People filters that define it, so this always reflects who matches now; uploaded lists and point-in-time snapshots match their recorded members.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — Only include the person linked to this whop user ID.
    
</dd>
</dl>

<dl>
<dd>

**email:** `Optional<String>` — Only include the person linked to this email address.
    
</dd>
</dl>

<dl>
<dd>

**phone:** `Optional<String>` — Only include the person linked to this phone number.
    
</dd>
</dl>

<dl>
<dd>

**country:** `Optional<String>` — Only include people whose most recent visit came from this ISO 3166-1 alpha-2 country code.
    
</dd>
</dl>

<dl>
<dd>

**hasPurchased:** `Optional<Boolean>` — true for customers only, false for people who have never purchased.
    
</dd>
</dl>

<dl>
<dd>

**contactable:** `Optional<Boolean>` — true for people who have an email address or phone number — the ones an ad platform can match.
    
</dd>
</dl>

<dl>
<dd>

**firstSeenWithinDays:** `Optional<Integer>` — Only include people first seen within this many days, as a rolling window.
    
</dd>
</dl>

<dl>
<dd>

**lastSeenWithinDays:** `Optional<Integer>` — Only include people last seen within this many days, as a rolling window.
    
</dd>
</dl>

<dl>
<dd>

**firstSeenAfter:** `Optional<OffsetDateTime>` — Only include people first seen at or after this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**firstSeenBefore:** `Optional<OffsetDateTime>` — Only include people first seen before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**lastSeenAfter:** `Optional<OffsetDateTime>` — Only include people last seen at or after this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**lastSeenBefore:** `Optional<OffsetDateTime>` — Only include people last seen before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of people to return (default 100, max 100).
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor for fetching people after a previous page.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — A cursor for fetching people before a later page.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListPeopleRequestOrder>` — Column to sort by. Defaults to last_seen_at.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListPeopleRequestDirection>` — Sort direction. Defaults to desc.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.people.retrieve(id) -> RetrievePeopleResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves one person for an account. The identifier can be a person ID (prefixed `prsn_`), a user ID (prefixed `user_`), an email address, or a phone number — merged people resolve to the surviving profile.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.people().retrieve(
    "id",
    RetrievePeopleRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The person ID, user ID, email address, or phone number to look up.
    
</dd>
</dl>

<dl>
<dd>

**accountId:** `Optional<String>` — Account ID, prefixed `biz_`. Optional for account API keys; required for credentials that can access multiple accounts.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Permissions
<details><summary><code>client.permissions.list() -> ListPermissionsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists permission actions and whether the calling credential is granted each one for a resource. Answers for whichever identity authenticated the request — a user session, an OAuth token, or an account or app API key — so it never describes who else can reach the resource.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.permissions().list(
    ListPermissionsRequest
        .builder()
        .resourceId("resource_id")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**resourceId:** `String` — Tag of the resource to check against: an account (`biz_`), product (`prod_`), experience (`exp_`), or app (`app_`). A resource the credential cannot see is reported as granted nothing rather than as an error.
    
</dd>
</dl>

<dl>
<dd>

**actions:** `Optional<String>` — Comma-separated permission actions to check, for example `stats:read,payment:basic:read`. Every action is returned when omitted.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Plans
<details><summary><code>client.plans.list() -> SyncPagingIterable&amp;lt;PlanListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of plans belonging to an account, with optional filtering by visibility, type, release method, and product.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.plans().list(
    ListPlansRequest
        .builder()
        .accountId("account_id")
        .releaseMethods(
            Arrays.asList("buy_now")
        )
        .visibilities(
            Arrays.asList("visible")
        )
        .planTypes(
            Arrays.asList("renewal")
        )
        .productIds(
            Arrays.asList("prod_xxxxxxxxxxxxxx")
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `String` — The unique identifier of the account to list plans for.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListPlansRequestDirection>` — The sort direction for results. Defaults to descending.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListPlansRequestOrder>` — The field to sort results by. Defaults to created_at.
    
</dd>
</dl>

<dl>
<dd>

**releaseMethods:** `Optional<String>` — Filter to only plans matching these release methods.
    
</dd>
</dl>

<dl>
<dd>

**visibilities:** `Optional<String>` — Filter to only plans matching these visibility states.
    
</dd>
</dl>

<dl>
<dd>

**planTypes:** `Optional<String>` — Filter to only plans matching these billing types.
    
</dd>
</dl>

<dl>
<dd>

**productIds:** `Optional<String>` — Filter to only plans belonging to these product identifiers.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Only return plans created before this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Only return plans created after this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of plans to return (default and max 100).
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor; returns plans after this position.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — The number of plans to return from the end of the range.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — A cursor; returns plans before this position.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.plans.create(request) -> Plan</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Create a new pricing plan for a product. The plan defines the billing interval, price, and availability for customers.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.plans().create(
    CreatePlansRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The unique identifier of the account to create this plan for. Defaults to the caller's account.
    
</dd>
</dl>

<dl>
<dd>

**adaptivePricingEnabled:** `Optional<Boolean>` — Whether this plan accepts local currency payments via adaptive pricing.
    
</dd>
</dl>

<dl>
<dd>

**billingPeriod:** `Optional<Integer>` — Recurring billing interval in days, such as 30 for monthly or 365 for annual.
    
</dd>
</dl>

<dl>
<dd>

**checkoutStyling:** `Optional<Map<String, Object>>` — Checkout styling overrides for this plan.
    
</dd>
</dl>

<dl>
<dd>

**currency:** `Optional<String>` — The three-letter ISO currency code for the plan's pricing. Defaults to USD.
    
</dd>
</dl>

<dl>
<dd>

**customFields:** `Optional<List<CreatePlansRequestCustomFieldsItem>>` — An array of custom field definitions to collect from customers at checkout. Omitting this field clears existing custom fields.
    
</dd>
</dl>

<dl>
<dd>

**description:** `Optional<String>` — A text description of the plan displayed to customers on the product page.
    
</dd>
</dl>

<dl>
<dd>

**expirationDays:** `Optional<Integer>` — Access duration in days before the membership expires.
    
</dd>
</dl>

<dl>
<dd>

**image:** `Optional<CreatePlansRequestImage>` — An image displayed on the product page to represent this plan.
    
</dd>
</dl>

<dl>
<dd>

**initialPrice:** `Optional<Double>` — Initial amount charged in the plan's currency, e.g. 10.43 for $10.43.
    
</dd>
</dl>

<dl>
<dd>

**internalNotes:** `Optional<String>` — Private notes visible only to the account owner. Not shown to customers.
    
</dd>
</dl>

<dl>
<dd>

**metadata:** `Optional<Map<String, Object>>` — Custom key-value pairs to store on the plan. Included in webhook payloads for payment and membership events. Max 50 keys, 100 chars per key, 500 chars per string value. The reserved keys `custom_cta` (a checkout call-to-action button label — one of the product custom CTA values, e.g. `subscribe`, `get_offer`) and `custom_cta_url` (a URL the button links to; web or `tel:`) override the product's call to action for this plan and are validated on save.
    
</dd>
</dl>

<dl>
<dd>

**overrideTaxType:** `Optional<String>` — Override the default tax classification for this specific plan.
    
</dd>
</dl>

<dl>
<dd>

**paymentMethodConfiguration:** `Optional<CreatePlansRequestPaymentMethodConfiguration>` — Explicit payment method configuration for the plan. When not provided, the account's defaults apply.
    
</dd>
</dl>

<dl>
<dd>

**planType:** `Optional<String>` — Plan billing type, such as `one_time` or `renewal`.
    
</dd>
</dl>

<dl>
<dd>

**productId:** `Optional<String>` — The unique identifier of the product to attach this plan to.
    
</dd>
</dl>

<dl>
<dd>

**releaseMethod:** `Optional<String>` — Sales method for this plan.
    
</dd>
</dl>

<dl>
<dd>

**renewalPrice:** `Optional<Double>` — The amount charged each billing period for recurring plans, in the plan's currency.
    
</dd>
</dl>

<dl>
<dd>

**splitPayRequiredPayments:** `Optional<Integer>` — Installment payments required before the subscription pauses.
    
</dd>
</dl>

<dl>
<dd>

**stock:** `Optional<Integer>` — The maximum number of units available for purchase. Ignored when unlimited_stock is true.
    
</dd>
</dl>

<dl>
<dd>

**threeDsLevel:** `Optional<CreatePlansRequestThreeDsLevel>` — 3D Secure behavior for this plan. Send `null` to inherit the account default.
    
</dd>
</dl>

<dl>
<dd>

**title:** `Optional<String>` — The display name of the plan shown to customers on the product page.
    
</dd>
</dl>

<dl>
<dd>

**trialPeriodDays:** `Optional<Integer>` — Free trial duration before the first recurring charge.
    
</dd>
</dl>

<dl>
<dd>

**unlimitedStock:** `Optional<Boolean>` — Whether the plan has unlimited stock. When true, the stock field is ignored.
    
</dd>
</dl>

<dl>
<dd>

**visibility:** `Optional<String>` — Whether the plan is visible to customers or hidden from public view.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.plans.retrieve(id) -> Plan</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing plan.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.plans().retrieve(
    "id",
    RetrievePlansRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Plan ID, prefixed `plan_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.plans.delete(id) -> DeletePlansResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Permanently delete a plan from a product. Existing memberships on this plan will not be affected.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.plans().delete(
    "id",
    DeletePlansRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Plan ID, prefixed `plan_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.plans.update(id, request) -> Plan</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Update a plan's pricing, billing interval, visibility, stock, and other settings.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.plans().update(
    "id",
    UpdatePlansRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Plan ID, prefixed `plan_`.
    
</dd>
</dl>

<dl>
<dd>

**adaptivePricingEnabled:** `Optional<Boolean>` — Whether this plan accepts local currency payments via adaptive pricing.
    
</dd>
</dl>

<dl>
<dd>

**billingPeriod:** `Optional<Integer>` — Recurring billing interval in days, such as 30 for monthly or 365 for annual.
    
</dd>
</dl>

<dl>
<dd>

**cancelDiscountIntervals:** `Optional<Integer>` — How many renewals the retention discount applies to. Required when `offer_cancel_discount` is true.
    
</dd>
</dl>

<dl>
<dd>

**cancelDiscountPercentage:** `Optional<Integer>` — Percentage taken off each discounted renewal. Required when `offer_cancel_discount` is true.
    
</dd>
</dl>

<dl>
<dd>

**checkoutStyling:** `Optional<Map<String, Object>>` — Checkout styling overrides for this plan.
    
</dd>
</dl>

<dl>
<dd>

**currency:** `Optional<String>` — The three-letter ISO currency code for the plan's pricing. Defaults to USD.
    
</dd>
</dl>

<dl>
<dd>

**customFields:** `Optional<List<UpdatePlansRequestCustomFieldsItem>>` — An array of custom field definitions to collect from customers at checkout. Omitting this field clears existing custom fields.
    
</dd>
</dl>

<dl>
<dd>

**description:** `Optional<String>` — A text description of the plan displayed to customers on the product page.
    
</dd>
</dl>

<dl>
<dd>

**expirationDays:** `Optional<Integer>` — Access duration in days before the membership expires.
    
</dd>
</dl>

<dl>
<dd>

**image:** `Optional<UpdatePlansRequestImage>` — An image displayed on the product page to represent this plan.
    
</dd>
</dl>

<dl>
<dd>

**initialPrice:** `Optional<Double>` — Initial amount charged in the plan's currency, e.g. 10.43 for $10.43.
    
</dd>
</dl>

<dl>
<dd>

**internalNotes:** `Optional<String>` — Private notes visible only to the account owner. Not shown to customers.
    
</dd>
</dl>

<dl>
<dd>

**metadata:** `Optional<Map<String, Object>>` — Custom key-value pairs to store on the plan. Included in webhook payloads for payment and membership events. Max 50 keys, 100 chars per key, 500 chars per string value. The reserved keys `custom_cta` (a checkout call-to-action button label — one of the product custom CTA values, e.g. `subscribe`, `get_offer`) and `custom_cta_url` (a URL the button links to; web or `tel:`) override the product's call to action for this plan and are validated on save.
    
</dd>
</dl>

<dl>
<dd>

**offerCancelDiscount:** `Optional<Boolean>` — Whether to offer a retention discount when a customer attempts to cancel.
    
</dd>
</dl>

<dl>
<dd>

**overrideTaxType:** `Optional<String>` — Override the default tax classification for this specific plan.
    
</dd>
</dl>

<dl>
<dd>

**paymentMethodConfiguration:** `Optional<UpdatePlansRequestPaymentMethodConfiguration>` — Explicit payment method configuration for the plan. When not provided, the account's defaults apply.
    
</dd>
</dl>

<dl>
<dd>

**releaseMethod:** `Optional<String>` — Sales method for this plan.
    
</dd>
</dl>

<dl>
<dd>

**renewalPrice:** `Optional<Double>` — The amount charged each billing period for recurring plans, in the plan's currency.
    
</dd>
</dl>

<dl>
<dd>

**stock:** `Optional<Integer>` — The maximum number of units available for purchase. Ignored when unlimited_stock is true.
    
</dd>
</dl>

<dl>
<dd>

**strikeThroughInitialPrice:** `Optional<Double>` — A comparison price displayed with a strikethrough for the initial price.
    
</dd>
</dl>

<dl>
<dd>

**strikeThroughRenewalPrice:** `Optional<Double>` — A comparison price displayed with a strikethrough for the renewal price.
    
</dd>
</dl>

<dl>
<dd>

**threeDsLevel:** `Optional<UpdatePlansRequestThreeDsLevel>` — 3D Secure behavior for this plan. Send `null` to inherit the account default.
    
</dd>
</dl>

<dl>
<dd>

**title:** `Optional<String>` — The display name of the plan shown to customers on the product page.
    
</dd>
</dl>

<dl>
<dd>

**trialPeriodDays:** `Optional<Integer>` — Free trial duration before the first recurring charge.
    
</dd>
</dl>

<dl>
<dd>

**unlimitedStock:** `Optional<Boolean>` — Whether the plan has unlimited stock. When true, the stock field is ignored.
    
</dd>
</dl>

<dl>
<dd>

**visibility:** `Optional<String>` — Whether the plan is visible to customers or hidden from public view.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.plans.calculateTax(id, request) -> CalculateTaxPlansResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Previews tax for a plan before checkout, based on the buyer's location.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.plans().calculateTax(
    "id",
    CalculateTaxPlansRequest
        .builder()
        .address(
            CalculateTaxPlansRequestAddress
                .builder()
                .country("DE")
                .postalCode("10115")
                .build()
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Plan ID, prefixed `plan_`.
    
</dd>
</dl>

<dl>
<dd>

**address:** `Optional<CalculateTaxPlansRequestAddress>` — Buyer billing address used for tax calculation. Provide either `address.country` or `ip_address`; include state and postal code when available for more accurate results.
    
</dd>
</dl>

<dl>
<dd>

**ipAddress:** `Optional<String>` — Buyer IP address used to infer location when no billing address is provided.
    
</dd>
</dl>

<dl>
<dd>

**taxIds:** `Optional<List<CalculateTaxPlansRequestTaxIdsItem>>` — Optional buyer tax ID for B2B exemptions. At most one entry is supported.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Products
<details><summary><code>client.products.list() -> SyncPagingIterable&amp;lt;ProductListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of products belonging to an account.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.products().list(
    ListProductsRequest
        .builder()
        .accountId("account_id")
        .visibilities(
            Arrays.asList("visible")
        )
        .accessPassTypes(
            Arrays.asList("regular")
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `String` — The unique identifier of the account to list products for.
    
</dd>
</dl>

<dl>
<dd>

**visibilities:** `Optional<String>` — Filter to only products matching these visibility states.
    
</dd>
</dl>

<dl>
<dd>

**accessPassTypes:** `Optional<String>` — Filter to only products matching these types.
    
</dd>
</dl>

<dl>
<dd>

**labels:** `Optional<String>` — Filter to only products carrying all of these labels. Labels are matched lowercased.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListProductsRequestDirection>` — The sort direction for results. Defaults to descending.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<String>` — The field to sort results by. Defaults to created_at.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of products to return (default and max 100).
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor; returns products after this position.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — The number of products to return from the end of the range.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — A cursor; returns products before this position.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.products.create(request) -> Product</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Creates a new product for an account.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.products().create(
    CreateProductsRequest
        .builder()
        .title("Interior Deep Clean")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The unique identifier of the account to create this product for.
    
</dd>
</dl>

<dl>
<dd>

**collectShippingAddress:** `Optional<Boolean>` — Whether to collect a shipping address at checkout.
    
</dd>
</dl>

<dl>
<dd>

**customCta:** `Optional<CreateProductsRequestCustomCta>` — The call-to-action button label.
    
</dd>
</dl>

<dl>
<dd>

**customCtaUrl:** `Optional<String>` — A URL the call-to-action button links to.
    
</dd>
</dl>

<dl>
<dd>

**customStatementDescriptor:** `Optional<String>` — Custom bank statement descriptor. Must start with WHOP*.
    
</dd>
</dl>

<dl>
<dd>

**description:** `Optional<String>` — A written description displayed on the product page.
    
</dd>
</dl>

<dl>
<dd>

**globalAffiliatePercentage:** `Optional<Double>` — The commission rate affiliates earn.
    
</dd>
</dl>

<dl>
<dd>

**globalAffiliateStatus:** `Optional<CreateProductsRequestGlobalAffiliateStatus>` — The enrollment status in the global affiliate program.
    
</dd>
</dl>

<dl>
<dd>

**headline:** `Optional<String>` — A short marketing headline for the product page.
    
</dd>
</dl>

<dl>
<dd>

**labels:** `Optional<List<String>>` — Labels used to group products into collections. Stored lowercased and de-duplicated. Maximum 20 labels, 50 characters each.
    
</dd>
</dl>

<dl>
<dd>

**memberAffiliatePercentage:** `Optional<Double>` — The commission rate members earn.
    
</dd>
</dl>

<dl>
<dd>

**memberAffiliateStatus:** `Optional<CreateProductsRequestMemberAffiliateStatus>` — The enrollment status in the member affiliate program.
    
</dd>
</dl>

<dl>
<dd>

**metadata:** `Optional<Map<String, Object>>` — Custom key-value pairs to store on the product.
    
</dd>
</dl>

<dl>
<dd>

**productTaxCodeId:** `Optional<String>` — The unique identifier of the tax classification code. See the available [product categories](https://docs.numeral.com/essentials/product-categories).
    
</dd>
</dl>

<dl>
<dd>

**redirectPurchaseUrl:** `Optional<String>` — A URL to redirect the customer to after purchase.
    
</dd>
</dl>

<dl>
<dd>

**route:** `Optional<String>` — The URL slug for the product's public link.
    
</dd>
</dl>

<dl>
<dd>

**sendWelcomeMessage:** `Optional<Boolean>` — Whether to send an automated welcome message via support chat when a user joins this product. Defaults to true.
    
</dd>
</dl>

<dl>
<dd>

**title:** `String` — The display name of the product. Maximum 80 characters.
    
</dd>
</dl>

<dl>
<dd>

**visibility:** `Optional<String>` — Whether the product is visible to customers.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.products.retrieve(id) -> Product</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing product. This endpoint is publicly accessible.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.products().retrieve(
    "id",
    RetrieveProductsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the product.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.products.delete(id) -> DeleteProductsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Deletes a product. Only products with no memberships, entries, reviews, or invoices can be deleted.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.products().delete(
    "id",
    DeleteProductsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the product.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.products.update(id, request) -> Product</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Updates an existing product.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.products().update(
    "id",
    UpdateProductsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the product.
    
</dd>
</dl>

<dl>
<dd>

**bannerImage:** `Optional<UpdateProductsRequestBannerImage>` — A wide image for the product, shown on the product page and on listing cards. Pass `{ id }` for an existing attachment or `{ direct_upload_id }` for a completed direct upload; `null` removes it.
    
</dd>
</dl>

<dl>
<dd>

**description:** `Optional<String>` — A written description displayed on the product page.
    
</dd>
</dl>

<dl>
<dd>

**headline:** `Optional<String>` — A short marketing headline for the product page.
    
</dd>
</dl>

<dl>
<dd>

**labels:** `Optional<List<String>>` — Labels used to group products into collections. Replaces the existing labels. Send an empty array to clear them.
    
</dd>
</dl>

<dl>
<dd>

**metadata:** `Optional<Map<String, Object>>` — Custom key-value pairs to store on the product.
    
</dd>
</dl>

<dl>
<dd>

**productTaxCodeId:** `Optional<String>` — The unique identifier of the tax classification code. See the available [product categories](https://docs.numeral.com/essentials/product-categories).
    
</dd>
</dl>

<dl>
<dd>

**sendWelcomeMessage:** `Optional<Boolean>` — Whether to send an automated welcome message via support chat when a user joins this product.
    
</dd>
</dl>

<dl>
<dd>

**title:** `Optional<String>` — The display name of the product.
    
</dd>
</dl>

<dl>
<dd>

**visibility:** `Optional<String>` — Whether the product is visible to customers.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.products.publish(id) -> Product</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Submits a product to the whop.com marketplace for review. The product moves to `pending_review`; a Whop reviewer approves it before it goes live.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.products().publish(
    "id",
    PublishProductsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the product, prefixed `prod_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.products.unpublish(id) -> Product</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Removes a product from the whop.com marketplace. The product moves to `not_available`.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.products().unpublish(
    "id",
    UnpublishProductsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the product, prefixed `prod_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Promo Codes
<details><summary><code>client.promoCodes.list() -> SyncPagingIterable&amp;lt;PromoCodeListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists promo codes for an account with cursor pagination, filters, and sorting.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.promoCodes().list(
    ListPromoCodesRequest
        .builder()
        .accountId("account_id")
        .productIds(
            Arrays.asList("prod_xxxxxxxxxxxxxx")
        )
        .planIds(
            Arrays.asList("plan_xxxxxxxxxxxxxx")
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `String` — Account whose promo codes are listed (`biz_` tag).
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<ListPromoCodesRequestStatus>` — Promo-code status. `expired` groups inactive and archived codes.
    
</dd>
</dl>

<dl>
<dd>

**productIds:** `Optional<String>` — Only promo codes scoped to these product IDs.
    
</dd>
</dl>

<dl>
<dd>

**planIds:** `Optional<String>` — Only promo codes scoped to these plan IDs.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<OffsetDateTime>` — Only promo codes created before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<OffsetDateTime>` — Only promo codes created after this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListPromoCodesRequestOrder>` — Sort field.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListPromoCodesRequestDirection>` — Sort direction.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Number of promo codes to return from the start of the window.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor to paginate forwards from.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Number of promo codes to return from the end of the window.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Cursor to paginate backwards from.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.promoCodes.create(request) -> PromoCode</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Creates a promo code for an account. First-party sessions may attach an affiliate.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.promoCodes().create(
    CreatePromoCodesRequest
        .builder()
        .accountId("biz_xxxxxxxxxxxxxx")
        .amountOff(25.0)
        .baseCurrency(CreatePromoCodesRequestBaseCurrency.USD)
        .code("AFFILIATE25")
        .newUsersOnly(true)
        .promoDurationMonths(3)
        .promoType(CreatePromoCodesRequestPromoType.PERCENTAGE)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**amountOff:** `Double` 
    
</dd>
</dl>

<dl>
<dd>

**baseCurrency:** `CreatePromoCodesRequestBaseCurrency` 
    
</dd>
</dl>

<dl>
<dd>

**churnedUsersOnly:** `Optional<Boolean>` 
    
</dd>
</dl>

<dl>
<dd>

**code:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**existingMembershipsOnly:** `Optional<Boolean>` 
    
</dd>
</dl>

<dl>
<dd>

**expiresAt:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**newUsersOnly:** `Boolean` 
    
</dd>
</dl>

<dl>
<dd>

**onePerCustomer:** `Optional<Boolean>` 
    
</dd>
</dl>

<dl>
<dd>

**planIds:** `Optional<List<String>>` 
    
</dd>
</dl>

<dl>
<dd>

**productId:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**promoDurationMonths:** `Integer` 
    
</dd>
</dl>

<dl>
<dd>

**promoType:** `CreatePromoCodesRequestPromoType` 
    
</dd>
</dl>

<dl>
<dd>

**stock:** `Optional<Integer>` 
    
</dd>
</dl>

<dl>
<dd>

**unlimitedStock:** `Optional<Boolean>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.promoCodes.retrieve(id) -> PromoCode</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a promo code by ID.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.promoCodes().retrieve(
    "id",
    RetrievePromoCodesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Promo code ID (`promo_` tag).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.promoCodes.delete(id) -> DeletePromoCodesResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Archives a promo code so it cannot be used in future checkouts.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.promoCodes().delete(
    "id",
    DeletePromoCodesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Promo code ID (`promo_` tag).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.promoCodes.activate(id) -> PromoCode</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Turns an inactive promo code back on so it can be redeemed at checkout.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.promoCodes().activate(
    "id",
    ActivatePromoCodesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Promo code ID (`promo_` tag).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.promoCodes.deactivate(id) -> PromoCode</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Turns off an active promo code so it can no longer be redeemed at checkout.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.promoCodes().deactivate(
    "id",
    DeactivatePromoCodesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Promo code ID (`promo_` tag).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Reactions
<details><summary><code>client.reactions.list() -> SyncPagingIterable&amp;lt;ReactionListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of emoji reactions on a specific message or forum post, sorted by most recent.

Required permissions (one of):
 - `chat:read`
 - `dms:read`
 - `forum:read`
 - `livestream:chat:read`
 - `support_chat:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.reactions().list(
    ListReactionsRequest
        .builder()
        .resourceId("resource_id")
        .first(42)
        .last(42)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**resourceId:** `String` — The unique identifier of the message or forum post to list reactions for.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.reactions.create(request) -> Reaction</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Add an emoji reaction or poll vote to a message or forum post. In forums, the reaction is always a like.

Required permissions (one of):
 - `chat:read`
 - `dms:read`
 - `forum:read`
 - `livestream:chat:read`
 - `support_chat:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.reactions().create(
    CreateReactionsRequest
        .builder()
        .resourceId("resource_id")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**emoji:** `Optional<String>` — The emoji to react with, in shortcode or unicode format. For example, ':heart:' or a unicode emoji. Ignored in forums where reactions are always likes.
    
</dd>
</dl>

<dl>
<dd>

**pollOptionId:** `Optional<String>` — The unique identifier of a poll option to vote for. Only valid when the target message or post contains a poll.
    
</dd>
</dl>

<dl>
<dd>

**resourceId:** `String` — The unique identifier of the message or forum post to react to.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.reactions.retrieve(id) -> Reaction</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing reaction.

Required permissions (one of):
 - `chat:read`
 - `dms:read`
 - `forum:read`
 - `livestream:chat:read`
 - `support_chat:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.reactions().retrieve(
    "reac_xxxxxxxxxxxxxxxxxxxxxx",
    RetrieveReactionsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the reaction to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.reactions.delete(id) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Remove an emoji reaction from a message or forum post. Only the reaction author or a channel admin can remove a reaction.

Required permissions (one of):
 - `chat:read`
 - `dms:read`
 - `forum:read`
 - `livestream:chat:read`
 - `support_chat:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.reactions().delete(
    "reac_xxxxxxxxxxxxxxxxxxxxxx",
    DeleteReactionsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the reaction to remove, or the identifier of the message or forum post to remove a reaction from. When passing a message or post ID, you must also provide the emoji argument.
    
</dd>
</dl>

<dl>
<dd>

**emoji:** `Optional<String>` — The emoji to remove, in shortcode or unicode format. For example, ':heart:' or a unicode emoji. Required when the id refers to a message or post instead of a reaction.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Recommended Actions
<details><summary><code>client.recommendedActions.list() -> ListRecommendedActionsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the recommended action chains for an account — short sequences of actions (create a product, price it, publish it) the account should run next, gated on what it already has.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.recommendedActions().list(
    ListRecommendedActionsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — Account ID, prefixed `biz_`. Defaults to the API key's own account.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.recommendedActions.retrieve(id) -> AccountRecommendedActionChain</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a recommended action chain by id, including chains that have already been run. Seeded chains are reconstructed from their hard-coded chain; generated chains are read from the account's stored chain, with each step's filled-in input.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.recommendedActions().retrieve(
    "id",
    RetrieveRecommendedActionsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Chain ID from the list endpoint, e.g. `rac_seed_start_selling_9f2c1a7b04`.
    
</dd>
</dl>

<dl>
<dd>

**accountId:** `Optional<String>` — Account ID, prefixed `biz_`. Defaults to the API key's own account.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.recommendedActions.run(id) -> RunRecommendedActionsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Records that the caller ran a recommended action chain. Nothing is executed server-side yet — the client follows the chain's step CTAs itself; this writes the `recommended_action_chain.executed` analytics event.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.recommendedActions().run(
    "id",
    RunRecommendedActionsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Chain ID from the list endpoint, e.g. `rac_seed_start_selling_9f2c1a7b04`.
    
</dd>
</dl>

<dl>
<dd>

**accountId:** `Optional<String>` — Account ID, prefixed `biz_`. Defaults to the API key's own account.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.recommendedActions.listExecutions(id) -> ListExecutionsRecommendedActionsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the per-step record of a recommended action chain the server ran — one entry per step in position order, each carrying its current status and, once the step completed, the API response it produced. A chain that was never run server-side returns an empty list.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.recommendedActions().listExecutions(
    "id",
    ListExecutionsRecommendedActionsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Chain ID from the list endpoint.
    
</dd>
</dl>

<dl>
<dd>

**accountId:** `Optional<String>` — Account ID, prefixed `biz_`. Defaults to the API key's own account.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Refunds
<details><summary><code>client.refunds.list() -> SyncPagingIterable&amp;lt;RefundListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of refunds, with optional filtering by payment, company, user, and creation date.

Required permissions:
 - `payment:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.refunds().list(
    ListRefundsRequest
        .builder()
        .first(42)
        .last(42)
        .paymentId("pay_xxxxxxxxxxxxxx")
        .companyId("biz_xxxxxxxxxxxxxx")
        .userId("user_xxxxxxxxxxxxx")
        .createdBefore(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .createdAfter(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**paymentId:** `Optional<String>` — Filter refunds to only those associated with this specific payment.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `Optional<String>` — Filter refunds to only those belonging to this company.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — Filter refunds to only those associated with this specific user.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<Direction>` 
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<OffsetDateTime>` — Only return refunds created before this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<OffsetDateTime>` — Only return refunds created after this timestamp.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.refunds.retrieve(id) -> Refund</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing refund.

Required permissions:
 - `payment:basic:read`
 - `plan:basic:read`
 - `access_pass:basic:read`
 - `member:email:read`
 - `member:basic:read`
 - `member:phone:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.refunds().retrieve(
    "rf_xxxxxxxxxxxxxxx",
    RetrieveRefundsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the refund.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Resolution Center Cases
<details><summary><code>client.resolutionCenterCases.list() -> SyncPagingIterable&amp;lt;ResolutionCenterCase&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists resolution center cases. Without `account_id` you get every case you can read — the ones you opened as a buyer and every account you are a team member of; the filters narrow that list.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.resolutionCenterCases().list(
    ListResolutionCenterCasesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — Only cases filed against this account (`biz_` tag). With read access to the account this lists its whole queue; without, only the cases you opened against it.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — Only cases opened by this customer — a `user_` tag, or `me` for the calling user. It narrows what you can already read, so `me` lists the cases you opened without the ones on accounts you are a team member of.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of cases to return (default 20, max 100).
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor; returns cases after this position.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — The number of cases to return from the end of the range.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — A cursor; returns cases before this position.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListResolutionCenterCasesRequestOrder>` — The field to sort cases by.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListResolutionCenterCasesRequestDirection>` — Sort direction.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<ListResolutionCenterCasesRequestStatusItem>` — Only cases in these statuses. Repeat the parameter to pass several — one paginated list covers all of them.
    
</dd>
</dl>

<dl>
<dd>

**reason:** `Optional<ListResolutionCenterCasesRequestReasonItem>` — Only cases opened for these reasons. Repeat the parameter to pass several.
    
</dd>
</dl>

<dl>
<dd>

**outcome:** `Optional<ListResolutionCenterCasesRequestOutcomeItem>` — Only closed cases that ended these ways. Repeat the parameter to pass several.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Only cases created before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Only cases created after this ISO 8601 timestamp.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.resolutionCenterCases.create(request) -> ResolutionCenterCase</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Opens a case, as the customer, against one of your own payments. Provide the payment (`receipt_id`), the `reason`, and a `message`.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.resolutionCenterCases().create(
    CreateResolutionCenterCasesRequest
        .builder()
        .message("The mobile detailer never showed up for the Ceramic Coating appointment.")
        .reason(CreateResolutionCenterCasesRequestReason.FRAUDULENT)
        .receiptId("pay_xxxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**attachments:** `Optional<List<CreateResolutionCenterCasesRequestAttachmentsItem>>` 
    
</dd>
</dl>

<dl>
<dd>

**message:** `String` — The customer's explanation.
    
</dd>
</dl>

<dl>
<dd>

**reason:** `CreateResolutionCenterCasesRequestReason` — What went wrong. Uses the same vocabulary as `/disputes`.
    
</dd>
</dl>

<dl>
<dd>

**receiptId:** `String` — The payment to open the case against (`pay_` tag).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.resolutionCenterCases.summary() -> SummaryResolutionCenterCasesResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Aggregates the same cases `GET /resolution_center_cases` lists, using the same filters. Use it to build status tabs and issue filters without paging the whole list.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.resolutionCenterCases().summary(
    SummaryResolutionCenterCasesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**groups:** `Optional<SummaryResolutionCenterCasesRequestGroupsItem>` — Which breakdowns to return, keyed by these names under `groups`. Repeat the parameter to ask for several; omit it for all of them.
    
</dd>
</dl>

<dl>
<dd>

**accountId:** `Optional<String>` — The account to summarize cases for (`biz_` tag).
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — Only cases opened by this customer — a `user_` tag, or `me` for the calling user.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<SummaryResolutionCenterCasesRequestStatusItem>` — Only cases in these statuses.
    
</dd>
</dl>

<dl>
<dd>

**reason:** `Optional<SummaryResolutionCenterCasesRequestReasonItem>` — Only cases opened for these reasons.
    
</dd>
</dl>

<dl>
<dd>

**outcome:** `Optional<SummaryResolutionCenterCasesRequestOutcomeItem>` — Only closed cases that ended these ways.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Only count cases created before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Only count cases created after this ISO 8601 timestamp.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.resolutionCenterCases.retrieve(id) -> ResolutionCenterCase</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a single resolution center case with its full event timeline.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.resolutionCenterCases().retrieve(
    "id",
    RetrieveResolutionCenterCasesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The resolution center case ID (`reso_` tag).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.resolutionCenterCases.accept(id, request) -> ResolutionCenterCase</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Accepts the case in the customer's favor, as the merchant: refunds the payment in full and closes the case.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.resolutionCenterCases().accept(
    "id",
    AcceptResolutionCenterCasesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The resolution center case ID (`reso_` tag).
    
</dd>
</dl>

<dl>
<dd>

**attachments:** `Optional<List<AcceptResolutionCenterCasesRequestAttachmentsItem>>` — Up to 3 evidence files, by existing file `id` or `direct_upload_id`.
    
</dd>
</dl>

<dl>
<dd>

**message:** `Optional<String>` — An optional note to the customer, recorded on the case timeline.
    
</dd>
</dl>

<dl>
<dd>

**terminateMembership:** `Optional<Boolean>` — Whether to also terminate the customer's membership.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.resolutionCenterCases.appeal(id, request) -> ResolutionCenterCase</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Appeals a decision, as the customer, on a case that closed in the merchant's favor. Escalates the case to Whop for platform review. A case can be appealed once.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.resolutionCenterCases().appeal(
    "id",
    AppealResolutionCenterCasesRequest
        .builder()
        .message("The coating is already flaking on the hood two weeks later.")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The resolution center case ID (`reso_` tag).
    
</dd>
</dl>

<dl>
<dd>

**attachments:** `Optional<List<AppealResolutionCenterCasesRequestAttachmentsItem>>` — Up to 3 evidence files, by existing file `id` or `direct_upload_id`.
    
</dd>
</dl>

<dl>
<dd>

**message:** `String` — Why you are appealing the decision.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.resolutionCenterCases.deny(id, request) -> ResolutionCenterCase</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Denies the case, as the merchant: rejects the claim and closes the case with no refund.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.resolutionCenterCases().deny(
    "id",
    DenyResolutionCenterCasesRequest
        .builder()
        .message("The ceramic coating was applied and the vehicle was collected on 2026-01-05.")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The resolution center case ID (`reso_` tag).
    
</dd>
</dl>

<dl>
<dd>

**attachments:** `Optional<List<DenyResolutionCenterCasesRequestAttachmentsItem>>` — Up to 3 evidence files, by existing file `id` or `direct_upload_id`.
    
</dd>
</dl>

<dl>
<dd>

**message:** `String` — Why the claim is being denied. Shown to the customer.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.resolutionCenterCases.events(id) -> SyncPagingIterable&amp;lt;ResolutionEvent&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the case timeline, newest first. Events the viewer is not allowed to see are omitted — a customer reads the customer-visible timeline, the merchant reads the full one.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.resolutionCenterCases().events(
    "id",
    EventsResolutionCenterCasesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The resolution center case ID (`reso_` tag).
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of events to return (default 20, max 100).
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor; returns events after this position.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — The number of events to return from the end of the range.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — A cursor; returns events before this position.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.resolutionCenterCases.reply(id, request) -> ResolutionCenterCase</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Replies to an open request for information on the case. As the merchant this answers Whop's request (valid while the case awaits your information); as the customer it provides the information requested from you. The actor is resolved from the credential.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.resolutionCenterCases().reply(
    "id",
    ReplyResolutionCenterCasesRequest
        .builder()
        .message("Here are the before and after photos from the Burnet Rd bay.")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The resolution center case ID (`reso_` tag).
    
</dd>
</dl>

<dl>
<dd>

**attachments:** `Optional<List<ReplyResolutionCenterCasesRequestAttachmentsItem>>` — Up to 3 evidence files, by existing file `id` or `direct_upload_id`.
    
</dd>
</dl>

<dl>
<dd>

**message:** `String` — The reply to add to the case.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.resolutionCenterCases.requestInfo(id, request) -> ResolutionCenterCase</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Asks the customer for more information, as the merchant. Allowed up to 3 times per case before you must accept or deny it.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.resolutionCenterCases().requestInfo(
    "id",
    RequestInfoResolutionCenterCasesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The resolution center case ID (`reso_` tag).
    
</dd>
</dl>

<dl>
<dd>

**attachments:** `Optional<List<RequestInfoResolutionCenterCasesRequestAttachmentsItem>>` — Up to 3 evidence files, by existing file `id` or `direct_upload_id`.
    
</dd>
</dl>

<dl>
<dd>

**message:** `Optional<String>` — What you need from the customer.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.resolutionCenterCases.withdraw(id) -> ResolutionCenterCase</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Withdraws (cancels) a case you opened, as the customer. Only possible while the case is still open.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.resolutionCenterCases().withdraw(
    "id",
    WithdrawResolutionCenterCasesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The resolution center case ID (`reso_` tag).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Reviews
<details><summary><code>client.reviews.list() -> SyncPagingIterable&amp;lt;ReviewListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of customer reviews for a specific product, with optional filtering by star rating and creation date.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.reviews().list(
    ListReviewsRequest
        .builder()
        .productId("prod_xxxxxxxxxxxxx")
        .first(42)
        .last(42)
        .minStars(42)
        .maxStars(42)
        .createdBefore(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .createdAfter(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**productId:** `String` — The unique identifier of the product to list reviews for.
    
</dd>
</dl>

<dl>
<dd>

**minStars:** `Optional<Integer>` — The minimum star rating to include in results, from 1 to 5 inclusive.
    
</dd>
</dl>

<dl>
<dd>

**maxStars:** `Optional<Integer>` — The maximum star rating to include in results, from 1 to 5 inclusive.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<OffsetDateTime>` — Only return reviews created before this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<OffsetDateTime>` — Only return reviews created after this timestamp.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.reviews.retrieve(id) -> Review</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing review.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.reviews().retrieve(
    "rev_xxxxxxxxxxxxxx",
    RetrieveReviewsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the review to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Setup Intents
<details><summary><code>client.setupIntents.list() -> SyncPagingIterable&amp;lt;SetupIntentListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of setup intents for a company, with optional filtering by creation date. A setup intent securely collects and stores a member's payment method for future use without charging them immediately.

Required permissions:
 - `payment:setup_intent:read`
 - `member:basic:read`
 - `member:email:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.setupIntents().list(
    ListSetupIntentsRequest
        .builder()
        .companyId("biz_xxxxxxxxxxxxxx")
        .first(42)
        .last(42)
        .createdBefore(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .createdAfter(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `String` — The unique identifier of the company to list setup intents for.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<Direction>` 
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<OffsetDateTime>` — Only return setup intents created before this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<OffsetDateTime>` — Only return setup intents created after this timestamp.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.setupIntents.create(request) -> CreateSetupIntentsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Save a buyer's payment method for later without charging it. Provide a confirmation token for a method the buyer just supplied, or an existing payment method to re-verify. The buyer may still have a step to complete — 3D Secure, a hosted enrollment, linking a bank account — so poll the setup intent's status endpoint for what to do next.

Required permissions:
 - `payment:charge`
 - `member:basic:read`
 - `member:email:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.setupIntents().create(
    CreateSetupIntentsRequest.of(
        CreateSetupIntentsRequestConfirmationToken
            .builder()
            .companyId("biz_xxxxxxxxxxxxxx")
            .confirmationToken("ctok_xxxxxxxxxxxxxx")
            .build()
    )
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**request:** `CreateSetupIntentsRequest` — Parameters for CreateSetupIntent
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.setupIntents.retrieve(id) -> SetupIntent</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing setup intent.

Required permissions:
 - `payment:setup_intent:read`
 - `member:basic:read`
 - `member:email:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.setupIntents().retrieve(
    "sint_xxxxxxxxxxxxx",
    RetrieveSetupIntentsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the setup intent.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.setupIntents.updateReturnUrl(setupIntentId, request) -> SetupStatus</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Changes where the buyer lands after completing an off-site step, up until they return. Accepts either a secret key or the setup's own `client_secret`, so the surface that knows the final destination can set it.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.setupIntents().updateReturnUrl(
    "setup_intent_id",
    UpdateReturnUrlSetupIntentsRequest
        .builder()
        .returnUrl("https://shinetime.example/checkout/thanks")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**setupIntentId:** `String` — The unique identifier of the setup intent.
    
</dd>
</dl>

<dl>
<dd>

**returnUrl:** `String` — Where the buyer continues after completing an off-site step. Must be an absolute https URL without credentials (http is allowed for localhost), at most 2,048 characters.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.setupIntents.retrieveStatus(setupIntentId) -> SetupStatus</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves how far a setup has got and what the buyer must do next, if anything. Collection runs in the background, so poll this rather than reading the create response. Accepts either a secret key or the setup's own `client_secret`, so the surface collecting the payment method can poll it directly.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.setupIntents().retrieveStatus(
    "setup_intent_id",
    RetrieveStatusSetupIntentsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**setupIntentId:** `String` — The unique identifier of the setup intent.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Shipments
<details><summary><code>client.shipments.list() -> SyncPagingIterable&amp;lt;Shipment&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of shipments for an account.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.shipments().list(
    ListShipmentsRequest
        .builder()
        .paymentId(
            Arrays.asList("pay_xxxxxxxxxxxxxx")
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The account to list shipments for. Defaults to the acting account.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<ListShipmentsRequestStatus>` — Filter to shipments with this delivery status.
    
</dd>
</dl>

<dl>
<dd>

**paymentId:** `Optional<String>` — Only shipments fulfilling these payments, each prefixed `pay_`. Repeat the parameter to pass several, up to 100 per request — one paginated list covers all of them.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Return shipments created before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Return shipments created after this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListShipmentsRequestOrder>` — The field to sort by.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListShipmentsRequestDirection>` — The sort direction.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of shipments to return.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor; returns shipments after this position.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — The number of shipments to return from the end of the range.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — A cursor; returns shipments before this position.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.shipments.create(request) -> Shipment</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Attaches a carrier tracking number to a payment and begins tracking it.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.shipments().create(
    CreateShipmentsRequest
        .builder()
        .paymentId("pay_xxxxxxxxxxxxxx")
        .trackingNumber("1Z999AA10123456784")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The unique identifier of the account, prefixed `biz_`.
    
</dd>
</dl>

<dl>
<dd>

**paymentId:** `String` — The payment to attach the shipment to, prefixed `pay_`.
    
</dd>
</dl>

<dl>
<dd>

**trackingNumber:** `String` — The carrier-assigned tracking number.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.shipments.retrieve(id) -> Shipment</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a shipment by its id, or by the payment id it fulfills.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.shipments().retrieve(
    "id",
    RetrieveShipmentsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The shipment id (`ship_`), or the payment id (`pay_`) it fulfills.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.shipments.update(id, request) -> Shipment</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Updates a shipment's tracking number and re-tracks it with the carrier.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.shipments().update(
    "id",
    UpdateShipmentsRequest
        .builder()
        .trackingNumber("9400111899223456789012")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The shipment id (`ship_`), or the payment id (`pay_`) it fulfills.
    
</dd>
</dl>

<dl>
<dd>

**trackingNumber:** `String` — The new carrier-assigned tracking number.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Social Accounts
<details><summary><code>client.socialAccounts.list() -> SyncPagingIterable&amp;lt;SocialAccount&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the social accounts linked to an account or user.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.socialAccounts().list(
    ListSocialAccountsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The Account that the social accounts are connected to. Provide either this or user_id.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — The User that the social accounts are connected to. Provide either this or account_id.
    
</dd>
</dl>

<dl>
<dd>

**platform:** `Optional<ListSocialAccountsRequestPlatform>` — Only return social accounts for the platform that is specified.
    
</dd>
</dl>

<dl>
<dd>

**verified:** `Optional<Boolean>` — Only return social accounts that are verified on the platform.
    
</dd>
</dl>

<dl>
<dd>

**scopes:** `Optional<ListSocialAccountsRequestScopesItem>` — Only return social accounts that have these scopes.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of social accounts to return.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor to fetch the page after (from page_info.end_cursor).
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — The number of social accounts to return from the end of the range.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Cursor to fetch the page before (from page_info.start_cursor).
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListSocialAccountsRequestOrder>` — The field to sort social accounts by.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListSocialAccountsRequestDirection>` — Sort direction.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.socialAccounts.create(request) -> SocialAccount</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Creates or returns a Whop-managed Facebook page for an account.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.socialAccounts().create(
    CreateSocialAccountsRequest
        .builder()
        .platform(CreateSocialAccountsRequestPlatform.FACEBOOK)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The Account (biz_ identifier) to create the social account for. An account-scoped API key may omit this to default to its own account. Account API keys cannot update their own account's branding through Update Account; use a user-authenticated path.
    
</dd>
</dl>

<dl>
<dd>

**platform:** `CreateSocialAccountsRequestPlatform` — The platform to create the social account on. `facebook` requires the account's `banner_image`, `logo`, and `description`; configure them with [Update Account](/api-reference/beta/accounts/update-account).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.socialAccounts.connect(request) -> ConnectSocialAccountsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Starts an OAuth connection flow and returns an authorize_url where the user can connect a social account.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.socialAccounts().connect(
    ConnectSocialAccountsRequest
        .builder()
        .platform(ConnectSocialAccountsRequestPlatform.META_BUSINESS)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The Account (biz_ identifier) to connect the social account for. An account-scoped API key may omit this to default to its own account.
    
</dd>
</dl>

<dl>
<dd>

**platform:** `ConnectSocialAccountsRequestPlatform` — The platform to connect the social account on. Supported options are `meta_business` and `tiktok`.
    
</dd>
</dl>

<dl>
<dd>

**redirectUrl:** `Optional<String>` — The Whop URL to redirect the user to after they finish connecting.
    
</dd>
</dl>

<dl>
<dd>

**scopes:** `Optional<List<ConnectSocialAccountsRequestScopesItem>>` — Capabilities to grant for the connected social account. Use `advertise` when connecting a Meta Business or TikTok account for ads.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.socialAccounts.delete(id) -> DeleteSocialAccountsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Disconnects a social account from an account or user without deleting the underlying platform account.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.socialAccounts().delete(
    "id",
    DeleteSocialAccountsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The ID of the social account to disconnect.
    
</dd>
</dl>

<dl>
<dd>

**accountId:** `Optional<String>` — The Account that the social account is connected to. Provide either this or user_id.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — The User that the social account is connected to. Provide either this or account_id.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.socialAccounts.leadForms(id) -> LeadFormsSocialAccountsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the active lead (instant) forms that already exist on a connected Facebook page, so an ad can reuse one as its `lead_gen_form_id` instead of authoring a new form. Every active form comes back in a single response — the list is not paginated.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.socialAccounts().leadForms(
    "id",
    LeadFormsSocialAccountsRequest
        .builder()
        .accountId("account_id")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The social account (a sacc_ identifier) whose lead forms to list.
    
</dd>
</dl>

<dl>
<dd>

**accountId:** `String` — The Account (a biz_ identifier) the social account is connected to.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.socialAccounts.posts(id) -> SyncPagingIterable&amp;lt;SocialAccountPost&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the existing posts of a connected Facebook page, Instagram account, or TikTok account.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.socialAccounts().posts(
    "id",
    PostsSocialAccountsRequest
        .builder()
        .accountId("account_id")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The social account (a sacc_ identifier) whose posts to list.
    
</dd>
</dl>

<dl>
<dd>

**accountId:** `String` — The Account (a biz_ identifier) the social account is connected to.
    
</dd>
</dl>

<dl>
<dd>

**postId:** `Optional<String>` — Return only the single post with this platform id, instead of the full list.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of posts to return.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor to fetch the page after (from page_info.end_cursor).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Stats
<details><summary><code>client.stats.list() -> ListStatsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists every metric you can query, with its unit and the properties you can filter or break it down by.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.stats().list();
```
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.stats.describeStats() -> DescribeStatsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Describe available stats schema. Without resource returns root nodes and metrics. With resource returns node columns, associations, and available metrics.

Required permissions:
 - `stats:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.stats().describeStats(
    DescribeStatsRequest
        .builder()
        .companyId("biz_xxxxxxxxxxxxxx")
        .userId("user_xxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**resource:** `Optional<String>` — Resource path using : as separator (e.g., 'receipts', 'payments:membership', 'receipts:gross_revenue').
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `Optional<String>` — Scope query to a specific company.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — Scope query to a specific user.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.stats.metricStats() -> MetricStatsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Query an aggregated metric. Returns data grouped by period with optional breakdowns.

Required permissions:
 - `stats:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.stats().metricStats(
    MetricStatsRequest
        .builder()
        .resource("resource")
        .from(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .to(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .companyId("biz_xxxxxxxxxxxxxx")
        .userId("user_xxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**resource:** `String` — Metric resource using : as separator (e.g., 'receipts:gross_revenue', 'members:new_users').
    
</dd>
</dl>

<dl>
<dd>

**granularity:** `Optional<String>` — Time granularity (daily, weekly, monthly).
    
</dd>
</dl>

<dl>
<dd>

**breakdowns:** `Optional<String>` — Columns to break down the metric by.
    
</dd>
</dl>

<dl>
<dd>

**filters:** `Optional<Map<String, Object>>` — Key-value pairs to filter the data.
    
</dd>
</dl>

<dl>
<dd>

**timeZone:** `Optional<String>` — IANA timezone for period bucketing (e.g. 'America/New_York'). Defaults to UTC. Only applies to ClickHouse metrics.
    
</dd>
</dl>

<dl>
<dd>

**from:** `Optional<OffsetDateTime>` — Start of time range (unix timestamp).
    
</dd>
</dl>

<dl>
<dd>

**to:** `Optional<OffsetDateTime>` — End of time range (unix timestamp).
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `Optional<String>` — Scope query to a specific company.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — Scope query to a specific user.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.stats.rawStats() -> RawStatsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Query raw data from a resource. Returns paginated rows with all columns.

Required permissions:
 - `stats:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.stats().rawStats(
    RawStatsRequest
        .builder()
        .resource("resource")
        .from(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .to(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .limit(42)
        .companyId("biz_xxxxxxxxxxxxxx")
        .userId("user_xxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**resource:** `String` — Resource path using : as separator (e.g., 'members', 'payments:membership').
    
</dd>
</dl>

<dl>
<dd>

**from:** `Optional<OffsetDateTime>` — Start of time range (unix timestamp).
    
</dd>
</dl>

<dl>
<dd>

**to:** `Optional<OffsetDateTime>` — End of time range (unix timestamp).
    
</dd>
</dl>

<dl>
<dd>

**limit:** `Optional<Integer>` — Number of records to return (max 10000).
    
</dd>
</dl>

<dl>
<dd>

**cursor:** `Optional<String>` — Pagination cursor for next page.
    
</dd>
</dl>

<dl>
<dd>

**sort:** `Optional<String>` — Column to sort by.
    
</dd>
</dl>

<dl>
<dd>

**sortDirection:** `Optional<Direction>` 
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `Optional<String>` — Scope query to a specific company.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — Scope query to a specific user.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.stats.retrieve(metric) -> RetrieveStatsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a metric as a time series of points for an account or user over a time range. The `market_prices` metric is public and requires no authentication.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.stats().retrieve(
    "metric",
    RetrieveStatsRequest
        .builder()
        .from("from")
        .to("to")
        .adCampaignIds(
            Arrays.asList("adcamp_xxxxxxxxxxxxxx")
        )
        .adGroupIds(
            Arrays.asList("adgrp_xxxxxxxxxxxxxx")
        )
        .adIds(
            Arrays.asList("ad_xxxxxxxxxxxxxx")
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**metric:** `String` — The metric to retrieve, for example net_revenue. Use GET /stats to see every metric key. The metric sets the unit and the properties you can filter or break down by.
    
</dd>
</dl>

<dl>
<dd>

**accountId:** `Optional<String>` — The account this query concerns, for example biz_AbC123.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — The user this query concerns, for example user_AbC123. Available on metrics that support user subjects, such as account_balance.
    
</dd>
</dl>

<dl>
<dd>

**from:** `String` — Start of the range — a date (YYYY-MM-DD), expanded to the start of that day, or an ISO 8601 timestamp (for example 2026-07-16T16:37:00Z), used exactly.
    
</dd>
</dl>

<dl>
<dd>

**to:** `String` — End of the range — a date (YYYY-MM-DD), expanded to the end of that day, or an ISO 8601 timestamp (for example 2026-07-17T16:37:00Z), used exactly.
    
</dd>
</dl>

<dl>
<dd>

**interval:** `Optional<RetrieveStatsRequestInterval>` — How wide each point is. Defaults to day. Snapshot metrics are day-only.
    
</dd>
</dl>

<dl>
<dd>

**breakdownBy:** `Optional<String>` — Split the metric out by one of its properties — each point gets a breakdown array. For example breakdown_by=currency returns an entry for usd, an entry for eur, and so on.
    
</dd>
</dl>

<dl>
<dd>

**convertTo:** `Optional<String>` — Display currency for money metrics — every amount is converted into this ISO currency using the exchange rate on each period's date. Defaults to usd. For the ads metrics (ad_spend, ad_delivery), pass the account's ads reporting currency to match the ad entity endpoints. On transaction metrics, it is ignored when you filter or break down by currency (those report the original transaction currency, unconverted).
    
</dd>
</dl>

<dl>
<dd>

**currency:** `Optional<String>` — Select the source currency or asset on metrics that list currency. For transaction metrics, for example currency=eur, values are reported without conversion. For market_prices, use btc or xaut and convert_to=usd. Pair with breakdown_by=currency to split a metric by currency.
    
</dd>
</dl>

<dl>
<dd>

**timeZone:** `Optional<String>` — IANA time zone to bucket the series in, for example America/New_York. Defaults to UTC. Not accepted by snapshot metrics, which are UTC only.
    
</dd>
</dl>

<dl>
<dd>

**paymentMethod:** `Optional<String>` — Filter to a single payment method, for example card or crypto. Available on metrics that list payment_method.
    
</dd>
</dl>

<dl>
<dd>

**cardNetwork:** `Optional<String>` — Filter to a single card brand, for example visa. A refinement of payment_method=card. Available on metrics that list card_network.
    
</dd>
</dl>

<dl>
<dd>

**disputeReason:** `Optional<String>` — Filter disputes to a normalized reason, for example product_not_received. Pair with breakdown_by=dispute_reason to split dispute counts by reason.
    
</dd>
</dl>

<dl>
<dd>

**source:** `Optional<String>` — Filter to a single GMV source, for example payments — or, on the traffic metrics, a visit source (whop_ads, direct, or a utm_source value). Pair with breakdown_by=source to split by source. Available on metrics that list source.
    
</dd>
</dl>

<dl>
<dd>

**hostname:** `Optional<String>` — Filter traffic metrics to one website hostname, for example shop.example.com. Pair with breakdown_by=hostname to split by website.
    
</dd>
</dl>

<dl>
<dd>

**page:** `Optional<String>` — Filter traffic metrics to one page — a hostname plus normalized path, for example shop.example.com/pricing. Pair with breakdown_by=page to split by page.
    
</dd>
</dl>

<dl>
<dd>

**deviceType:** `Optional<String>` — Filter traffic metrics to one device type: desktop, mobile, tablet, or unknown. Pair with breakdown_by=device_type to split by device.
    
</dd>
</dl>

<dl>
<dd>

**countryCode:** `Optional<String>` — Filter traffic metrics to one visitor country (uppercase ISO 3166-1 alpha-2, for example US). Pair with breakdown_by=country_code to split by country.
    
</dd>
</dl>

<dl>
<dd>

**eventName:** `Optional<String>` — Filter the events metric to one tracked event name, for example pixel.page or pixel.custom. Pair with breakdown_by=event_name to split by event.
    
</dd>
</dl>

<dl>
<dd>

**eventType:** `Optional<RetrieveStatsRequestEventType>` — Filter the events metric to a canonical group of events: page_view (pixel page views plus whop.com store views), checkout_start (hosted and embedded checkout views), or other. Pair with breakdown_by=event_type to split by group.
    
</dd>
</dl>

<dl>
<dd>

**customName:** `Optional<String>` — Filter the events metric to one merchant-defined custom event name. Only valid alongside event_name=pixel.custom. Pair with breakdown_by=custom_name to split custom events by name.
    
</dd>
</dl>

<dl>
<dd>

**segment:** `Optional<String>` — Filter to a single wallet-balance segment, for example available. Pair with breakdown_by=segment to split the balance. Available on metrics that list segment.
    
</dd>
</dl>

<dl>
<dd>

**category:** `Optional<String>` — Filter to a single balance-activity category, for example payments. Pair with breakdown_by=category to split the activity. Available on metrics that list category.
    
</dd>
</dl>

<dl>
<dd>

**merchant:** `Optional<String>` — Filter to a single cashback merchant bucket, for example whop-ads. Pair with breakdown_by=merchant to split cashback by merchant. Available on metrics that list merchant.
    
</dd>
</dl>

<dl>
<dd>

**feeType:** `Optional<String>` — Filter to a single fee type. Pair with breakdown_by=fee_type to split fees by type. Available on metrics that list fee_type.
    
</dd>
</dl>

<dl>
<dd>

**product:** `Optional<String>` — Filter to a single product (access pass id), for example prod_AbC123. Pair with breakdown_by=product. Available on metrics that list product.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<String>` — Filter to a single membership status. Pair with breakdown_by=status. Available on metrics that list status.
    
</dd>
</dl>

<dl>
<dd>

**accessLevel:** `Optional<String>` — Filter to a single access level. Pair with breakdown_by=access_level. Available on metrics that list access_level.
    
</dd>
</dl>

<dl>
<dd>

**mostRecentAction:** `Optional<String>` — Filter to a single most-recent member action. Pair with breakdown_by=most_recent_action. Available on metrics that list most_recent_action.
    
</dd>
</dl>

<dl>
<dd>

**referredUserId:** `Optional<String>` — Filter a referral metric to the businesses attributed to one person you referred, for example user_AbC123. Available on metrics that list referred_user_id.
    
</dd>
</dl>

<dl>
<dd>

**adCampaignIds:** `Optional<String>` — Ad campaign ids (adcamp_...) to scope the report to; stats are summed across them. Available on metrics that list ad_campaign_ids.
    
</dd>
</dl>

<dl>
<dd>

**adGroupIds:** `Optional<String>` — Ad group ids (adgrp_...) to scope the report to; stats are summed across them. Available on metrics that list ad_group_ids.
    
</dd>
</dl>

<dl>
<dd>

**adIds:** `Optional<String>` — Ad ids (ad_...) to scope the report to; stats are summed across them. Available on metrics that list ad_ids.
    
</dd>
</dl>

<dl>
<dd>

**snapshotWindow:** `Optional<RetrieveStatsRequestSnapshotWindow>` — Window used by a snapshot metric. Ordinary snapshots accept 30d as their trailing activity window. Cohorted dispute metrics accept 7d or 28d as the sales-transaction pool; their attribution window is fixed in the metric name. Each metric lists its accepted values in the catalog.
    
</dd>
</dl>

<dl>
<dd>

**event:** `Optional<String>` — Filter the events metric to one or more full event names, for example payment.completed or pixel.lead. Comma-separate several to break the metric down by each event. Available on metrics that list event.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## SupportChannels
<details><summary><code>client.supportChannels.list() -> SyncPagingIterable&amp;lt;SupportChannelListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of support channels for a specific company, with optional filtering by resolution status and custom sorting.

Required permissions:
 - `support_chat:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.supportChannels().list(
    ListSupportChannelsRequest
        .builder()
        .first(42)
        .last(42)
        .companyId("biz_xxxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `Optional<String>` — The unique identifier of the company to list support channels for. Includes channels of child companies. When omitted, returns support channels across all companies the user has access to.
    
</dd>
</dl>

<dl>
<dd>

**view:** `Optional<SupportChannelView>` 
    
</dd>
</dl>

<dl>
<dd>

**open:** `Optional<Boolean>` — Whether to filter by open or resolved support channels. Set to true to only return channels awaiting a response, or false for resolved channels.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<Direction>` 
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<MessageChannelOrder>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.supportChannels.create(request) -> SupportChannel</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Open a new support channel between a company team member and a customer. Returns the existing channel if one already exists for that user.

Required permissions:
 - `support_chat:create`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.supportChannels().create(
    CreateSupportChannelsRequest
        .builder()
        .companyId("biz_xxxxxxxxxxxxxx")
        .userId("user_xxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**companyId:** `String` — The unique identifier of the company to create the support channel in.
    
</dd>
</dl>

<dl>
<dd>

**customName:** `Optional<String>` — Optional custom display name for the support channel.
    
</dd>
</dl>

<dl>
<dd>

**notificationsEnabled:** `Optional<Boolean>` — Whether Whop app notifications are enabled for this support channel. Webhooks still fire.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `String` — The user ID (e.g. 'user_xxxxx') or username of the customer to open a support channel for.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.supportChannels.retrieve(id) -> SupportChannel</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing support channel.

Required permissions:
 - `support_chat:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.supportChannels().retrieve(
    "id",
    RetrieveSupportChannelsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the support channel to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Swaps
<details><summary><code>client.swaps.list() -> ListSwapsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieve the account's completed or pending swaps — currently just the latest one.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.swaps().list(
    ListSwapsRequest
        .builder()
        .accountId("account_id")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `String` — Business or user account ID (biz_* / user_*).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.swaps.create(request) -> CreateSwapsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Swaps one token for another from the account's wallet, or converts between fiat currencies in the account's ledger at the mid-market rate. Crypto swaps finish in the background — check the swap for its status.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.swaps().create(
    CreateSwapsRequest
        .builder()
        .accountId("biz_xxxxxxxxxxxxxx")
        .fromToken("usd")
        .toToken("cad")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `String` — Business or user account ID (biz_* / user_*).
    
</dd>
</dl>

<dl>
<dd>

**amount:** `Optional<String>` — Source token amount. Required for crypto swaps. For fiat pairs: the amount of from_token to convert at the mid-market rate; omit (along with to_amount) to repay the full negative to_token balance instead.
    
</dd>
</dl>

<dl>
<dd>

**fromChain:** `Optional<CreateSwapsRequestFromChain>` — Source chain name or chain ID. Defaults to the source token's chain when omitted.
    
</dd>
</dl>

<dl>
<dd>

**fromToken:** `String` — Source token contract address or ticker symbol, such as "USDT".
    
</dd>
</dl>

<dl>
<dd>

**slippageBps:** `Optional<Integer>` — Maximum slippage tolerance in basis points.
    
</dd>
</dl>

<dl>
<dd>

**toAmount:** `Optional<String>` — Fiat pairs only: sizes a partial repayment of the negative to_token balance, denominated in to_token. Must not exceed the debt. Mutually exclusive with amount.
    
</dd>
</dl>

<dl>
<dd>

**toChain:** `Optional<CreateSwapsRequestToChain>` — Destination chain name or chain ID. Defaults to the destination token's chain when omitted.
    
</dd>
</dl>

<dl>
<dd>

**toToken:** `String` — Destination token contract address or ticker symbol, such as "XAUT".
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.swaps.createQuote(request) -> CreateQuoteSwapsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Previews the price of a swap. Fiat pairs quote the in-ledger mid-market conversion — the same rate creating the swap fills at. No funds move and nothing is saved.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.swaps().createQuote(
    CreateQuoteSwapsRequest
        .builder()
        .amount("100")
        .fromToken("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
        .toToken("0x1b64b9025eebb9a6239575df9ea4b9ac46d4d193")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**amount:** `String` — Source token amount.
    
</dd>
</dl>

<dl>
<dd>

**fromAddress:** `Optional<String>` — Source wallet address used for the quote.
    
</dd>
</dl>

<dl>
<dd>

**fromChain:** `Optional<CreateQuoteSwapsRequestFromChain>` — Source chain name or chain ID. Defaults to the source token's chain when omitted.
    
</dd>
</dl>

<dl>
<dd>

**fromToken:** `String` — Source token contract address or ticker symbol, such as "USDT".
    
</dd>
</dl>

<dl>
<dd>

**metadata:** `Optional<Map<String, Object>>` — Metadata to include with the quote response.
    
</dd>
</dl>

<dl>
<dd>

**slippageBps:** `Optional<Integer>` — Maximum slippage tolerance in basis points.
    
</dd>
</dl>

<dl>
<dd>

**toAddress:** `Optional<String>` — Destination wallet address used for the quote.
    
</dd>
</dl>

<dl>
<dd>

**toChain:** `Optional<CreateQuoteSwapsRequestToChain>` — Destination chain name or chain ID. Defaults to the destination token's chain when omitted.
    
</dd>
</dl>

<dl>
<dd>

**toToken:** `String` — Destination token contract address or ticker symbol, such as "XAUT".
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.swaps.retrieve(id) -> RetrieveSwapsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a single swap and its status.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.swaps().retrieve(
    "id",
    RetrieveSwapsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Swap ID returned from POST /swaps.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Team Members
<details><summary><code>client.teamMembers.list() -> SyncPagingIterable&amp;lt;TeamMember&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists an account's team members, including pending invites (`status: "pending"`, `ausri_` ids; `user` is `null` for invites sent to an email with no Whop account yet). For accepted members, `email` requires the `company:authorized_user:email:read` scope and is `null` otherwise. Listing `role=workforce` is also allowed with the `bounty:create` scope.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.teamMembers().list(
    ListTeamMembersRequest
        .builder()
        .accountId("account_id")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `String` — Account ID, prefixed `biz_`.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<ListTeamMembersRequestStatus>` — Only return members with this status: `joined` (accepted members) or `pending` (pending invites). Both are returned by default.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — Only return the membership for this user ID, prefixed `user_`.
    
</dd>
</dl>

<dl>
<dd>

**role:** `Optional<ListTeamMembersRequestRole>` — Only return members with this role. `custom` matches members on a dashboard-managed custom role.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Only return members added before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Only return members added after this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListTeamMembersRequestOrder>` — Field used to sort members.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListTeamMembersRequestDirection>` — Sort direction. Defaults to `desc`.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Number of members to return. Defaults to 20; maximum 100.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor for the next page of members.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Number of members to return from the end of the window.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Cursor to paginate backwards from.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.teamMembers.create(request) -> TeamMember</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Adds a member to an account's team with a system role. Identify them by exactly one of `user_id` or `email`. If the person has not yet accepted — or the email does not belong to a Whop account yet — an invitation is sent instead and the response is `202` with `{ "object": "team_member_invite", "invitation_sent": true }`. If they already have a pending invite, the request fails with a `400`. Custom roles cannot be granted via the API. Granting the `workforce` role is also allowed with the `bounty:create` scope.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.teamMembers().create(
    CreateTeamMembersRequest
        .builder()
        .accountId("biz_xxxxxxxxxxxxxx")
        .role(CreateTeamMembersRequestRole.OWNER)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `String` — Account ID, prefixed `biz_`.
    
</dd>
</dl>

<dl>
<dd>

**email:** `Optional<String>` — Email address to invite. Mutually exclusive with `user_id`. If the email already belongs to a Whop account it is treated the same as passing that account's `user_id`; otherwise a pending invite is created for the email.
    
</dd>
</dl>

<dl>
<dd>

**role:** `CreateTeamMembersRequestRole` — The system role to grant.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — The user to add to the team, prefixed `user_`. Mutually exclusive with `email`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.teamMembers.retrieve(id) -> TeamMember</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a team member by ID. `email` requires the `company:authorized_user:email:read` scope and is `null` otherwise.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.teamMembers().retrieve(
    "id",
    RetrieveTeamMembersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Team member ID — `ausr_` for accepted members, `ausri_` for pending invites.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.teamMembers.delete(id) -> DeleteTeamMembersResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Removes a team member from the account, or revokes a pending invite when given an `ausri_` ID. A user session may delete its own membership to leave the team without the delete scope. Removing a member on the `workforce` role is also allowed with the `bounty:create` scope. The account owner cannot be removed.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.teamMembers().delete(
    "id",
    DeleteTeamMembersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Team member ID — `ausr_` for accepted members, `ausri_` for pending invites.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.teamMembers.update(id, request) -> TeamMember</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Changes a team member's system role. Requires a user session — account API keys cannot change member roles. The account owner's role cannot be changed, and you cannot change your own role.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.teamMembers().update(
    "id",
    UpdateTeamMembersRequest
        .builder()
        .role(UpdateTeamMembersRequestRole.OWNER)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Team member ID — `ausr_` for accepted members, `ausri_` for pending invites.
    
</dd>
</dl>

<dl>
<dd>

**role:** `UpdateTeamMembersRequestRole` — The system role to grant.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Topups
<details><summary><code>client.topups.create(request) -> Topup</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Add funds to a company's platform balance by charging a stored payment method. Top-ups have no fees or taxes and do not count as revenue.

Required permissions:
 - `payment:charge`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.topups().create(
    CreateTopupsRequest
        .builder()
        .amount(6.9)
        .companyId("biz_xxxxxxxxxxxxxx")
        .currency(Currencies.USD)
        .paymentMethodId("pmt_xxxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**amount:** `Double` — The amount to add to the balance in the specified currency. For example, 50.00 for $50.00 USD.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `String` — The unique identifier of the company to add funds to, starting with 'biz_'.
    
</dd>
</dl>

<dl>
<dd>

**currency:** `Currencies` — The currency for the top-up amount, such as 'usd'.
    
</dd>
</dl>

<dl>
<dd>

**paymentMethodId:** `String` — The unique identifier of the stored payment method to charge for the top-up.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Transfers
<details><summary><code>client.transfers.list() -> SyncPagingIterable&amp;lt;ListTransfersResponseDataItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists an account's transfers.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.transfers().list(
    ListTransfersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**originId:** `Optional<String>` — Filter to transfers sent from this account. Provide this or destination_id.
    
</dd>
</dl>

<dl>
<dd>

**destinationId:** `Optional<String>` — Filter to transfers received by this account. Provide this or origin_id.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListTransfersRequestOrder>` — Sort column. Defaults to created_at.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListTransfersRequestDirection>` — Sort direction. Defaults to desc.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Only transfers created strictly before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Only transfers created strictly after this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Number of transfers to return from the start of the window.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor to fetch the page after (from page_info.end_cursor).
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Number of transfers to return from the end of the window.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Cursor to fetch the page before (from page_info.start_cursor).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.transfers.create(request) -> CreateTransfersResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Moves money between accounts, or into a claim link anyone with the URL can redeem.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.transfers().create(
    CreateTransfersRequest
        .builder()
        .amount(25.0)
        .originId("biz_xxxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**amount:** `Double` — The amount to move, in the transfer currency. For example 25.00.
    
</dd>
</dl>

<dl>
<dd>

**currency:** `Optional<String>` — Currency, such as `usd`. Required for ledger transfers.
    
</dd>
</dl>

<dl>
<dd>

**destinationId:** `Optional<String>` — The recipient. Required for ledger and wallet_send (a user_/biz_/ldgr_ ID, or — for sends — an email). Omit for claim_link.
    
</dd>
</dl>

<dl>
<dd>

**expiresAt:** `Optional<OffsetDateTime>` — claim_link only. Link expiry as an ISO 8601 timestamp. Defaults to 24 hours from creation.
    
</dd>
</dl>

<dl>
<dd>

**idempotenceKey:** `Optional<String>` — Ledger transfers and wallet sends. A unique key that makes retries safe. Retrying with the same key returns the original transfer, or attaches to the original wallet send, instead of moving money twice.
    
</dd>
</dl>

<dl>
<dd>

**metadata:** `Optional<Map<String, Object>>` — Ledger transfers only. Custom key-value pairs attached to the transfer. Max 50 keys, 100 chars per key, 500 chars per string value.
    
</dd>
</dl>

<dl>
<dd>

**notes:** `Optional<String>` — Ledger transfers only. A short note describing the transfer.
    
</dd>
</dl>

<dl>
<dd>

**originId:** `String` — The account sending the funds. A user ID (user_xxx), account ID (biz_xxx), or ledger account ID (ldgr_xxx).
    
</dd>
</dl>

<dl>
<dd>

**redeemableCount:** `Optional<Integer>` — claim_link only. How many different users can claim the link. Defaults to 1.
    
</dd>
</dl>

<dl>
<dd>

**type:** `Optional<CreateTransfersRequestType>` — The kind of money movement, which decides what comes back. Defaults to ledger. `ledger` moves credit between two Whop balances and returns a `transfer`; `wallet_send` sends USDT from the origin account's Ethereum wallet and returns a `send`; `claim_link` funds a shareable link anyone with the URL can redeem and returns a `claim_link`. A `ledger` transfer from a stablecoin-rails account settles on-chain when covered, and still returns a `transfer`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.transfers.listRecipients() -> SyncPagingIterable&amp;lt;ListRecipientsTransfersResponseDataItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the people and accounts you can send money to.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.transfers().listRecipients(
    ListRecipientsTransfersRequest
        .builder()
        .originId("origin_id")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**originId:** `String` — The account sending the money: a company account ID (`biz_`), or a user ID (`user_`) for that user's own personal balance.
    
</dd>
</dl>

<dl>
<dd>

**query:** `Optional<String>` — Search anyone on Whop by name or username, plus your own accounts by name or ID. Omit it to get the team around the balance, the people you follow, and your own accounts. The list is the same whether the balance belongs to a company or to you. Searching from a `biz_` origin additionally requires the member:basic:read scope. A credential scoped to a single company is the exception to the search itself: it only ever sees that company's own people. Complete email addresses return no matches.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Number of recipients per page. Search queries preserve the dashboard's 20-result maximum.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor to fetch the page after (from page_info.end_cursor).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.transfers.retrieve(id) -> RetrieveTransfersResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a single transfer.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.transfers().retrieve(
    "id",
    RetrieveTransfersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The transfer ID.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Users
<details><summary><code>client.users.list() -> SyncPagingIterable&amp;lt;User&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Search for users by name or username, ranked by social proximity to the authenticated user. Returns the user's most recently followed users when no query is given.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.users().list(
    ListUsersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**query:** `Optional<String>` — A search term to filter users by name or username.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of users to return (max 50).
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor; returns users after this position.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — The number of users to return from the end of the range.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — A cursor; returns users before this position.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.users.me() -> User</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the authenticated user — the self view of the user object. Same shape as `GET /users/{id}`, with the self-only fields populated: `email` (email-read scope), `staff` (Whop staff only, staff-read scope), `balance` and `earnings_usd` (balance-read scope), the opt-in `balance_history`, and every linked social account.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.users().me(
    MeUsersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — When set, returns your account-specific profile overrides for this account.
    
</dd>
</dl>

<dl>
<dd>

**includeBalanceHistory:** `Optional<Boolean>` — Also compute your balance history (opt-in; runs a heavier query). Ignored for callers without balance-read scope.
    
</dd>
</dl>

<dl>
<dd>

**from:** `Optional<String>` — Balance-history window start, ISO 8601 date or datetime. Defaults to 30 days ago. Only used with `include_balance_history`.
    
</dd>
</dl>

<dl>
<dd>

**to:** `Optional<String>` — Balance-history window end, ISO 8601 date or datetime. Defaults to now. Only used with `include_balance_history`.
    
</dd>
</dl>

<dl>
<dd>

**interval:** `Optional<MeUsersRequestInterval>` — Balance-history point granularity. Defaults to `day`. Only used with `include_balance_history`.
    
</dd>
</dl>

<dl>
<dd>

**timeZone:** `Optional<String>` — IANA time zone the balance-history points are bucketed in. Defaults to `UTC`. Only used with `include_balance_history`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.users.updateMe(request) -> User</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Updates the authenticated user's global profile, or their profile override for an account when account_id is given. Not available to API keys.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.users().updateMe(
    UpdateMeUsersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — When set, updates the authenticated user's profile override for this account instead of their global profile.
    
</dd>
</dl>

<dl>
<dd>

**banner:** `Optional<UpdateMeUsersRequestBanner>` 
    
</dd>
</dl>

<dl>
<dd>

**bio:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**name:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**profilePicture:** `Optional<UpdateMeUsersRequestProfilePicture>` 
    
</dd>
</dl>

<dl>
<dd>

**username:** `Optional<String>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.users.retrieve(id) -> User</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a user by `user_` tag or username, or the authenticated user with the reserved id `me`. Profiles include linked social accounts — reading your own profile returns every linked account, other profiles only what is public on Whop (the primary Discord and the X account). The self-only fields are populated only when the id is `me`: `email` (email-read scope), `staff` (Whop staff only, staff-read scope), `balance` and `earnings_usd` (balance-read scope), and the opt-in `balance_history`. They are always `null` when addressing a user by tag or username.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.users().retrieve(
    "id",
    RetrieveUsersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — User ID (prefixed `user_`), username, or `me` for the authenticated user.
    
</dd>
</dl>

<dl>
<dd>

**accountId:** `Optional<String>` — When set, returns the user's account-specific profile overrides for this account.
    
</dd>
</dl>

<dl>
<dd>

**includeBalanceHistory:** `Optional<Boolean>` — Also compute your balance history (opt-in; runs a heavier query). Only applies when the id is `me`; ignored for callers without balance-read scope.
    
</dd>
</dl>

<dl>
<dd>

**from:** `Optional<String>` — Balance-history window start, ISO 8601 date or datetime. Defaults to 30 days ago. Only used with `include_balance_history`.
    
</dd>
</dl>

<dl>
<dd>

**to:** `Optional<String>` — Balance-history window end, ISO 8601 date or datetime. Defaults to now. Only used with `include_balance_history`.
    
</dd>
</dl>

<dl>
<dd>

**interval:** `Optional<RetrieveUsersRequestInterval>` — Balance-history point granularity. Defaults to `day`. Only used with `include_balance_history`.
    
</dd>
</dl>

<dl>
<dd>

**timeZone:** `Optional<String>` — IANA time zone the balance-history points are bucketed in. Defaults to `UTC`. Only used with `include_balance_history`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.users.update(id, request) -> User</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Updates a user, addressed by `user_` tag, username, or the reserved id `me` for the authenticated user. A user token updates their own global profile; an API key updates the user's account-specific profile override (account_id required).
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.users().update(
    "id",
    UpdateUsersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — User ID (prefixed `user_`), username, or `me` for the authenticated user.
    
</dd>
</dl>

<dl>
<dd>

**accountId:** `Optional<String>` — The account whose profile override to update. Required for API key callers.
    
</dd>
</dl>

<dl>
<dd>

**banner:** `Optional<UpdateUsersRequestBanner>` 
    
</dd>
</dl>

<dl>
<dd>

**bio:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**name:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**profilePicture:** `Optional<UpdateUsersRequestProfilePicture>` 
    
</dd>
</dl>

<dl>
<dd>

**username:** `Optional<String>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.users.checkAccess(id, resourceId) -> CheckAccessUsersResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Checks whether a user has access to an account, product, or experience the caller can reach.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.users().checkAccess(
    "id",
    "resource_id",
    CheckAccessUsersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The user_ tag or username to check access for.
    
</dd>
</dl>

<dl>
<dd>

**resourceId:** `String` — An account (biz_), product (prod_), or experience (exp_) ID.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.users.recommendActions(id) -> RecommendActionsUsersResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the recommended actions computed for the user: personal suggestions (e.g. start a business or become an affiliate) pooled with the highest-impact actions across the accounts the user owns. Business actions are tagged with their `account_id`/`account_name`; personal actions leave those `null`. Self-only: `id` must be `me` or the authenticated user's own tag/username.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.users().recommendActions(
    "id",
    RecommendActionsUsersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — `me`, or the authenticated user's own `user_` tag or username.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Verifications
<details><summary><code>client.verifications.list() -> ListVerificationsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns verifications for an account, including their status and any required actions.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.verifications().list(
    ListVerificationsRequest
        .builder()
        .accountId("account_id")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `String` — Account or user ID whose verifications you want to list. Use a `biz_` account ID, or the caller's `user_` ID for personal verifications.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListVerificationsRequestOrder>` — Field used to sort returned verifications.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListVerificationsRequestDirection>` — Sort direction for returned verifications.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.verifications.create(request) -> CreateVerificationsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Starts a hosted verification session for an account or user, or returns the active session when one already exists. Any fields you include in the request body are used to prefill the session. Send `documents` (with `document_type`) to instead verify the person from identity documents included in this request — no hosted session involved. Send `share_token` to reuse a verification another Sumsub account has already completed for this person, instead of verifying them again. If the account already has an `approved` verification the request is rejected; unlink it first to start a new one.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.verifications().create(
    CreateVerificationsRequest
        .builder()
        .accountId("account_id")
        .body(
            CreateVerificationsRequestBody.individual(
                CreateVerificationsRequestBodyIndividual
                    .builder()
                    .build()
            )
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `String` — Account or user ID whose identity you want to verify. Use a `biz_` account ID for account verifications, or the caller's `user_` ID for personal verification.
    
</dd>
</dl>

<dl>
<dd>

**request:** `CreateVerificationsRequestBody` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.verifications.retrieve(id) -> RetrieveVerificationsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns verifications for an account, including their status and any required actions.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.verifications().retrieve(
    "id",
    RetrieveVerificationsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Verification profile ID, prefixed `idpf_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.verifications.update(id, request) -> UpdateVerificationsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Updates editable profile details or submits answers for items returned in `requested_information`. Once a verification is `approved` its profile details are locked and can no longer be edited.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.verifications().update(
    "id",
    UpdateVerificationsRequest
        .builder()
        .body(
            UpdateVerificationsRequestBody.of(
                UpdateVerificationsRequestBodyPersonalAddress
                    .builder()
                    .build()
            )
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Verification profile ID, prefixed `idpf_`.
    
</dd>
</dl>

<dl>
<dd>

**request:** `UpdateVerificationsRequestBody` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Webhooks
<details><summary><code>client.webhooks.list() -> SyncPagingIterable&amp;lt;WebhookListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of webhook endpoints configured for an account, ordered by most recently created.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.webhooks().list(
    ListWebhooksRequest
        .builder()
        .accountId("account_id")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `String` — The unique identifier of the account to list webhooks for.
    
</dd>
</dl>

<dl>
<dd>

**appId:** `Optional<String>` — Only return webhooks attached to this app. Omit to list the account's own webhooks.
    
</dd>
</dl>

<dl>
<dd>

**includeAppWebhooks:** `Optional<Boolean>` — Also return webhooks attached to the account's apps, not just the account's own. Cannot be combined with `app_id`.
    
</dd>
</dl>

<dl>
<dd>

**hasFailures:** `Optional<Boolean>` — Only return webhooks whose endpoint is currently failing — every delivery since the current failure streak began has been rejected. Clears as soon as a delivery succeeds.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of webhooks to return (default 20, max 100).
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor; returns webhooks after this position.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — The number of webhooks to return from the end of the range.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — A cursor; returns webhooks before this position.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.webhooks.create(request) -> Webhook</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Creates a webhook endpoint that receives event notifications via HTTP POST.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.webhooks().create(
    CreateWebhooksRequest
        .builder()
        .url("https://example.com/hooks")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**apiVersionDate:** `Optional<String>` — The dated API version (Api-Version-Date) to pin this webhook's payloads to. Omit to leave the webhook unpinned, tracking the current payload shape.
    
</dd>
</dl>

<dl>
<dd>

**childResourceEvents:** `Optional<Boolean>` — Whether to send events for child resources. For example, if the webhook is created for an account, enabling this sends events only from its connected accounts.
    
</dd>
</dl>

<dl>
<dd>

**enabled:** `Optional<Boolean>` — Whether or not the webhook is enabled. Defaults to `true`.
    
</dd>
</dl>

<dl>
<dd>

**events:** `Optional<List<CreateWebhooksRequestEventsItem>>` — The events to send the webhook for, in dot form (for example `payment.succeeded`).
    
</dd>
</dl>

<dl>
<dd>

**resourceId:** `Optional<String>` — The account or app to create the webhook for. Defaults to the current account.
    
</dd>
</dl>

<dl>
<dd>

**url:** `String` — The URL to send the webhook to.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.webhooks.retrieve(id) -> Webhook</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing webhook.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.webhooks().retrieve(
    "id",
    RetrieveWebhooksRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Webhook ID, prefixed `hook_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.webhooks.delete(id) -> DeleteWebhooksResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Permanently deletes a webhook endpoint. Returns `true` on success, matching the legacy proxy response.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.webhooks().delete(
    "id",
    DeleteWebhooksRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Webhook ID, prefixed `hook_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.webhooks.update(id, request) -> Webhook</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Updates a webhook endpoint's URL, subscribed events, pinned payload version, or enabled state.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.webhooks().update(
    "id",
    UpdateWebhooksRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Webhook ID, prefixed `hook_`.
    
</dd>
</dl>

<dl>
<dd>

**apiVersionDate:** `Optional<String>` — The dated API version (Api-Version-Date) to pin this webhook's payloads to. Only valid for `v1` webhooks. Omit to leave the current pin unchanged, or pass `null` to unpin and track the current payload shape.
    
</dd>
</dl>

<dl>
<dd>

**childResourceEvents:** `Optional<Boolean>` — Whether or not to send events for child resources.
    
</dd>
</dl>

<dl>
<dd>

**enabled:** `Optional<Boolean>` — Whether or not the webhook is enabled.
    
</dd>
</dl>

<dl>
<dd>

**events:** `Optional<List<UpdateWebhooksRequestEventsItem>>` — The events to send the webhook for, in dot form (for example `payment.succeeded`).
    
</dd>
</dl>

<dl>
<dd>

**url:** `Optional<String>` — The URL to send the webhook to.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.webhooks.listDeliveries(id) -> SyncPagingIterable&amp;lt;WebhookDelivery&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of delivery attempts for a webhook, ordered by most recent first. Includes the request payload, response body, response code, and timing for each attempt.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.webhooks().listDeliveries(
    "id",
    ListDeliveriesWebhooksRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Webhook ID, prefixed `hook_`.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of deliveries to return (default 50, max 100).
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor; returns deliveries after this position.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.webhooks.replayDelivery(id, deliveryId, request) -> ReplayDeliveryWebhooksResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Re-sends the exact payload of a past delivery to the webhook's current URL and returns the delivery result. By default the replay keeps the original `webhook-id`, so consumers that deduplicate on it can drop events they already processed. Pass `regenerate_id` to re-send under a freshly generated `webhook-id` instead, so a deduplicating consumer processes the replay as a new message. Only available for enabled webhooks on API version v1; deliveries are retained for 30 days.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.webhooks().replayDelivery(
    "id",
    "delivery_id",
    ReplayDeliveryWebhooksRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Webhook ID, prefixed `hook_`.
    
</dd>
</dl>

<dl>
<dd>

**deliveryId:** `String` — Delivery ID, prefixed `whdel_`, from the List Deliveries endpoint.
    
</dd>
</dl>

<dl>
<dd>

**regenerateId:** `Optional<Boolean>` — Re-send the delivery under a freshly generated `webhook-id` (in both the envelope and the signed headers) instead of the original one. Defaults to false. Use this when your endpoint deduplicates on `webhook-id` and you want it to process the replay as a new message.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.webhooks.replay(id, request) -> ReplayWebhooksResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Re-sends the webhook's past deliveries within a time window, optionally limited to specific events or to messages whose most recent delivery attempt failed. Fire and forget: nothing about the replay is stored, and each re-send appears as a new entry in the webhook's delivery log. Each matching message is re-sent once, by default with its original `webhook-id`, so consumers that deduplicate are unaffected; pass `regenerate_ids` to re-send under freshly generated ids instead. Only available for enabled webhooks on API version v1; deliveries are retained for 30 days.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.webhooks().replay(
    "id",
    ReplayWebhooksRequest
        .builder()
        .sentAfter("2026-01-01T12:00:00.000Z")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Webhook ID, prefixed `hook_`.
    
</dd>
</dl>

<dl>
<dd>

**events:** `Optional<List<String>>` — Only replay these event types, in dot form (for example `payment.succeeded`). Omit to include every event.
    
</dd>
</dl>

<dl>
<dd>

**failedOnly:** `Optional<Boolean>` — Only replay messages whose most recent delivery attempt in the window failed. Defaults to false. Best-effort: a message whose attempts span processing batches can still be re-sent — replays keep the original `webhook-id` by default, so consumers that deduplicate are unaffected.
    
</dd>
</dl>

<dl>
<dd>

**regenerateIds:** `Optional<Boolean>` — Re-send each replayed message under a freshly generated `webhook-id` (in both the envelope and the signed headers) instead of its original one. Defaults to false. Use this when your endpoint deduplicates on `webhook-id` and you want it to process the replays as new messages.
    
</dd>
</dl>

<dl>
<dd>

**sentAfter:** `String` — Start of the delivery window to replay, as an ISO 8601 timestamp. Clamped to the 30-day delivery retention.
    
</dd>
</dl>

<dl>
<dd>

**sentBefore:** `Optional<String>` — End of the delivery window to replay, as an ISO 8601 timestamp. Defaults to now.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.webhooks.test(id, request) -> TestWebhooksResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Sends a sample payload for the given event to the webhook's URL and returns the delivery result.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.webhooks().test(
    "id",
    TestWebhooksRequest
        .builder()
        .event("payment.succeeded")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Webhook ID, prefixed `hook_`.
    
</dd>
</dl>

<dl>
<dd>

**event:** `String` — The event to test the webhook for, in dot form (for example `payment.succeeded`).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.webhooks.deliveriesWebhook(webhookId) -> SyncPagingIterable&amp;lt;DeliveriesWebhookResponseDataItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of delivery attempts for a webhook, ordered by most recent first. Includes the request payload, response body, response code, and timing for each attempt.

Required permissions:
 - `developer:manage_webhook`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.webhooks().deliveriesWebhook(
    "webhook_id",
    DeliveriesWebhookRequest
        .builder()
        .first(42)
        .last(42)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**webhookId:** `String` — The unique identifier of the webhook to list deliveries for.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Withdrawals
<details><summary><code>client.withdrawals.list() -> SyncPagingIterable&amp;lt;WithdrawalListItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of withdrawals for a company, with optional sorting and date filtering.

Required permissions:
 - `payout:withdrawal:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.withdrawals().list(
    ListWithdrawalsRequest
        .builder()
        .companyId("biz_xxxxxxxxxxxxxx")
        .first(42)
        .last(42)
        .createdBefore(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .createdAfter(OffsetDateTime.parse("2023-12-01T05:00:00Z"))
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `String` — The unique identifier of the company to list withdrawals for.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<Direction>` 
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<OffsetDateTime>` — Only return withdrawals created before this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<OffsetDateTime>` — Only return withdrawals created after this timestamp.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.withdrawals.create(request) -> Withdrawal</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Creates a withdrawal request for a ledger account

Required permissions:
 - `payout:withdraw_funds`
 - `payout:destination:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.withdrawals().create(
    CreateWithdrawalsRequest
        .builder()
        .amount(6.9)
        .companyId("biz_xxxxxxxxxxxxxx")
        .currency(Currencies.USD)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**acknowledgeBankWarning:** `Optional<Boolean>` — Set to true to continue when the bank could not confirm the account holder's name, or false to be refused in that case so the creator can fix the account or link their bank first. Omitting the argument skips the warning gate — a client that cannot show the warning keeps its pre-gate behavior.
    
</dd>
</dl>

<dl>
<dd>

**amount:** `Double` — The amount to withdraw in the specified currency
    
</dd>
</dl>

<dl>
<dd>

**companyId:** `String` — The ID of the company to withdraw from.
    
</dd>
</dl>

<dl>
<dd>

**currency:** `Currencies` — The currency that is being withdrawn.
    
</dd>
</dl>

<dl>
<dd>

**idempotencyKey:** `Optional<String>` — A client-generated key that makes retries safe. Retrying with the same key returns the original withdrawal instead of creating a second one.
    
</dd>
</dl>

<dl>
<dd>

**payoutMethodId:** `Optional<String>` — The ID of the payout method to use for the withdrawal.
    
</dd>
</dl>

<dl>
<dd>

**platformCoversFees:** `Optional<Boolean>` — Whether the platform covers the payout fees.
    
</dd>
</dl>

<dl>
<dd>

**speed:** `Optional<WithdrawalSpeeds>` — The processing speed for the withdrawal. Either standard or instant.
    
</dd>
</dl>

<dl>
<dd>

**statementDescriptor:** `Optional<String>` — Custom statement descriptor for the withdrawal. Must be between 5 and 22 characters and contain only alphanumeric characters.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.withdrawals.retrieve(id) -> Withdrawal</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of an existing withdrawal.

Required permissions:
 - `payout:withdrawal:read`
 - `payout:destination:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.withdrawals().retrieve(
    "wdrl_xxxxxxxxxxxxx",
    RetrieveWithdrawalsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the withdrawal to retrieve.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.withdrawals.generatePdf(id) -> GeneratePdfWithdrawalsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Generates a withdrawal PDF invoice and returns a temporary download URL.

Required permissions:
 - `payout:withdrawal:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.withdrawals().generatePdf(
    "wdrl_xxxxxxxxxxxxx",
    GeneratePdfWithdrawalsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The unique identifier of the withdrawal to generate a PDF for.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Accounts Preferences
<details><summary><code>client.accounts.preferences.retrieve(accountId) -> RetrievePreferencesResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the account's preferences: a singleton settings document keyed by preference name.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.accounts().preferences().retrieve(
    "account_id",
    RetrievePreferencesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `String` — Account ID, prefixed `biz_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.accounts.preferences.update(accountId, request) -> UpdatePreferencesResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Updates the account's preferences. Each top-level key present in the body is replaced as a whole; omitted keys are left untouched. `ads_triple_whale_integration` takes the Data-In API key to connect with, or `null` to disconnect. `ads_payment_methods` always requires a `primary` entry. `backup` is optional and any pairing is allowed — two cards, `card`+`platform_balance`, or a single method — so a card-only advertiser can fund ads without a platform balance. The `primary` and `backup` must be different sources. A `platform_balance` entry may omit `id` to use the account's default Whop balance. Configuring a `card` requires a user token; account API keys can set up platform-balance billing only.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.accounts().preferences().update(
    "account_id",
    UpdatePreferencesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `String` — Account ID, prefixed `biz_`.
    
</dd>
</dl>

<dl>
<dd>

**adsPaymentMethods:** `Optional<UpdatePreferencesRequestAdsPaymentMethods>` — How the account pays for Whop Ads spend. `primary` is charged first; `backup` covers the charge when the primary fails.
    
</dd>
</dl>

<dl>
<dd>

**adsReportingCurrency:** `Optional<String>` — Lowercase ISO currency code, such as `usd` or `eur`, used to display ad spend and stats. Defaults to `usd`.
    
</dd>
</dl>

<dl>
<dd>

**adsSchedulingTimezone:** `Optional<String>` — IANA timezone (e.g. `America/New_York`) used to interpret campaign start/end times and to bucket reports. Cannot be cleared once set — pass a new value to change it.
    
</dd>
</dl>

<dl>
<dd>

**adsTripleWhaleIntegration:** `Optional<UpdatePreferencesRequestAdsTripleWhaleIntegration>` — Connects or disconnects the Triple Whale integration. Requires a connected Shopify store, since Triple Whale keys spend records by Shopify shop.
    
</dd>
</dl>

<dl>
<dd>

**cardsAutoTopUp:** `Optional<Boolean>` — Whether incoming funds are automatically moved to the account's cards balance. Requires a cards balance on the account.
    
</dd>
</dl>

<dl>
<dd>

**disputeFighterEnabled:** `Optional<Boolean>` — Whether Whop assembles and files the evidence response when this account's payments are disputed. Off by default; enabling it also opts the account into the success fee charged only on disputes it wins.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Accounts Reserves
<details><summary><code>client.accounts.reserves.list(accountId) -> ListReservesResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists what the account's held balance is made of, one entry per currency: the total held, why each part is held, and the days it unlocks.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.accounts().reserves().list(
    "account_id",
    ListReservesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `String` — Account ID, prefixed `biz_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Affiliates Overrides
<details><summary><code>client.affiliates.overrides.list(id) -> SyncPagingIterable&amp;lt;ListOverridesResponseDataItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns a paginated list of overrides for an affiliate.

Required permissions:
 - `affiliate:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.affiliates().overrides().list(
    "aff_xxxxxxxxxxxxxx",
    ListOverridesRequest
        .builder()
        .first(42)
        .last(42)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The affiliate ID.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Returns the elements in the list that come after the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Returns the elements in the list that come before the specified cursor.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Returns the first _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Returns the last _n_ elements from the list.
    
</dd>
</dl>

<dl>
<dd>

**overrideType:** `Optional<AffiliateOverrideRoles>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.affiliates.overrides.create(id, request) -> CreateOverridesResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Creates a commission override for an affiliate.

Required permissions:
 - `affiliate:create`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.affiliates().overrides().create(
    "aff_xxxxxxxxxxxxxx",
    CreateOverridesRequest
        .builder()
        .body(
            CreateOverridesRequestBody.standard(
                CreateOverridesRequestBodyStandard
                    .builder()
                    .commissionValue(6.9)
                    .id("id")
                    .planId("plan_xxxxxxxxxxxxx")
                    .build()
            )
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The affiliate ID.
    
</dd>
</dl>

<dl>
<dd>

**request:** `CreateOverridesRequestBody` — Parameters for CreateAffiliateOverride
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.affiliates.overrides.retrieve(id, overrideId) -> RetrieveOverridesResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the details of a specific affiliate override.

Required permissions:
 - `affiliate:basic:read`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.affiliates().overrides().retrieve(
    "aff_xxxxxxxxxxxxxx",
    "override_id",
    RetrieveOverridesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The affiliate ID.
    
</dd>
</dl>

<dl>
<dd>

**overrideId:** `String` — The override ID.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.affiliates.overrides.delete(id, overrideId) -> Boolean</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Deletes an affiliate override.

Required permissions:
 - `affiliate:update`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.affiliates().overrides().delete(
    "aff_xxxxxxxxxxxxxx",
    "override_id",
    DeleteOverridesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The affiliate ID.
    
</dd>
</dl>

<dl>
<dd>

**overrideId:** `String` — The override ID.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.affiliates.overrides.update(id, overrideId, request) -> UpdateOverridesResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Updates an existing affiliate override.

Required permissions:
 - `affiliate:update`
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.affiliates().overrides().update(
    "aff_xxxxxxxxxxxxxx",
    "override_id",
    UpdateOverridesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The affiliate ID.
    
</dd>
</dl>

<dl>
<dd>

**overrideId:** `String` — The override ID.
    
</dd>
</dl>

<dl>
<dd>

**appliesToPayments:** `Optional<AffiliateAppliesToPayments>` — Whether commission applies to first payment or all payments (standard only).
    
</dd>
</dl>

<dl>
<dd>

**commissionType:** `Optional<AffiliatePayoutTypes>` — The commission type (percentage or flat_fee).
    
</dd>
</dl>

<dl>
<dd>

**commissionValue:** `Optional<Double>` — The commission value (percentage 1-100 or flat fee in dollars).
    
</dd>
</dl>

<dl>
<dd>

**revenueBasis:** `Optional<AffiliateRevenueBases>` — The revenue calculation basis (rev-share only).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Bounties Submissions
<details><summary><code>client.bounties.submissions.list(bountyId) -> SyncPagingIterable&amp;lt;PublicBountySubmission&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists a bounty's publicly visible work — submitted, approved, and denied submissions in the reduced public shape. Authentication is optional; a bounty that is not publicly visible returns `404`.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.bounties().submissions().list(
    "bounty_id",
    ListSubmissionsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**bountyId:** `String` — The bounty whose public submissions to list (`bnty_` tag).
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<ListSubmissionsRequestStatus>` — Filter by lifecycle state.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Only submissions created after this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Only submissions created before this ISO 8601 timestamp.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListSubmissionsRequestOrder>` — Sort field.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListSubmissionsRequestDirection>` — Sort direction.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Number of submissions to return from the start of the window.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor to paginate forwards from.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Number of submissions to return from the end of the window.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Cursor to paginate backwards from.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.bounties.submissions.retrieve(bountyId, id) -> PublicBountySubmission</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves one of a bounty's publicly visible submissions in the reduced public shape — the read behind a shared proof link, whose submission is usually outside the bounty page's capped preview. Authentication is optional; a bounty that is not publicly visible, and a submission that is not publicly visible work on it, both return `404`.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.bounties().submissions().retrieve(
    "bounty_id",
    "id",
    RetrieveSubmissionsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**bountyId:** `String` — The bounty the submission belongs to (`bnty_` tag).
    
</dd>
</dl>

<dl>
<dd>

**id:** `String` — The submission to retrieve (`btys_` tag).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Members Logs
<details><summary><code>client.members.logs.list(id) -> SyncPagingIterable&amp;lt;ListLogsResponseDataItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists activity for a member and all of their non-drafted memberships, most recent first.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.members().logs().list(
    "id",
    ListLogsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Member ID (`mber_` tag).
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Number of log entries to return from the start of the window.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor to paginate forwards from.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Number of log entries to return from the end of the window.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Cursor to paginate backwards from.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Notifications Topics
<details><summary><code>client.notifications.topics.list() -> SyncPagingIterable&amp;lt;NotificationTopic&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the platform's visible notification topics — the categories users can set notification preferences on. App-created topics are internal and not returned.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.notifications().topics().list(
    ListTopicsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**topicType:** `Optional<ListTopicsRequestTopicType>` — Only return topics of this scope: `user` (member notifications) or `account_team` (team notifications).
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of topics to return (default 20, max 100).
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor; returns topics after this position.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Partners Businesses
<details><summary><code>client.partners.businesses.list() -> SyncPagingIterable&amp;lt;ListBusinessesResponseDataItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the businesses the authenticated user referred onto Whop, most recent first.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.partners().businesses().list(
    ListBusinessesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**status:** `Optional<ListBusinessesRequestStatus>` — Filter by referral status.
    
</dd>
</dl>

<dl>
<dd>

**hasEarnings:** `Optional<Boolean>` — When true, only businesses with pending or completed earnings paid to the caller.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Number of partner businesses to return from the start of the window.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor to fetch the page after (from page_info.end_cursor).
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Number of partner businesses to return from the end of the window.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Cursor to fetch the page before (from page_info.start_cursor).
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListBusinessesRequestOrder>` — The field to sort partner businesses by.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListBusinessesRequestDirection>` — Sort direction.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Only return partner businesses created before this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Only return partner businesses created after this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**referredUserId:** `Optional<String>` — Filter to referrals attributed to this user. For first-tier referrals, this is the referred account owner; for second-tier referrals, this is the partner you recruited.
    
</dd>
</dl>

<dl>
<dd>

**referredUsername:** `Optional<String>` — Filter by the referred user's exact username. Ignored when `referred_user_id` is present.
    
</dd>
</dl>

<dl>
<dd>

**tier:** `Optional<ListBusinessesRequestTier>` — Filter to only first-tier referrals or only second-tier referrals.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.partners.businesses.retrieve(id) -> RetrieveBusinessesResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves a single referred business and its referral terms.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.partners().businesses().retrieve(
    "id",
    RetrieveBusinessesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The partner business ID (a coma_ identifier).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Partners Businesses Earnings
<details><summary><code>client.partners.businesses.earnings.list(id) -> SyncPagingIterable&amp;lt;ListEarningsResponseDataItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the earnings Whop pays out for one referred business's activity, most recent first.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.partners().businesses().earnings().list(
    "id",
    ListEarningsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — The partner business ID (a coma_ identifier).
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<ListEarningsRequestStatus>` — Filter by earning status.
    
</dd>
</dl>

<dl>
<dd>

**incomeSource:** `Optional<ListEarningsRequestIncomeSourceItem>` — Filter to earnings from these income sources. Repeat the parameter for each one (income_source=sales&income_source=ad_spend).
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` 
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` 
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListEarningsRequestOrder>` — The field to sort earnings by.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListEarningsRequestDirection>` — Sort direction.
    
</dd>
</dl>

<dl>
<dd>

**createdBefore:** `Optional<String>` — Only return earnings created before this timestamp.
    
</dd>
</dl>

<dl>
<dd>

**createdAfter:** `Optional<String>` — Only return earnings created after this timestamp.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Payouts Methods
<details><summary><code>client.payouts.methods.list() -> SyncPagingIterable&amp;lt;ListMethodsResponseDataItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the bank accounts, wallets, and crypto addresses an account or user can withdraw to, newest first.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.payouts().methods().list(
    ListMethodsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The owning account ID (a biz_ identifier). Provide this or user_id.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — The owning user ID (a user_ identifier). Provide this or account_id.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<ListMethodsRequestStatus>` — Optional status filter. `created` means saved but unused, `active` means a payout through it succeeded, `broken` means the last payout failed and the method needs fixing.
    
</dd>
</dl>

<dl>
<dd>

**amount:** `Optional<Double>` — Optional withdrawal amount in whole currency units, for example `250.00`. When provided, each method includes a quote with the estimated fee, amount received, and delivery date for that amount.
    
</dd>
</dl>

<dl>
<dd>

**currency:** `Optional<String>` — Currency code of the amount, for example `usd`. Only meaningful with amount or include_limits.
    
</dd>
</dl>

<dl>
<dd>

**includeLimits:** `Optional<Boolean>` — When true, the response also carries limits — the live per-speed payout caps the account's payout requests are validated against, in the requested currency. Requires the payout:withdrawal:read scope.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Number of payout methods to return from the start of the window.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor to fetch the page after (from page_info.end_cursor).
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Number of payout methods to return from the end of the window.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Cursor to fetch the page before (from page_info.start_cursor).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.payouts.methods.create(request) -> CreateMethodsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Saves a new place an account or user can withdraw to. Sensitive details are vaulted in transit and never stored raw.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.payouts().methods().create(
    CreateMethodsRequest
        .builder()
        .supportedPayoutMethodId("podst_xxxxxxxxxxxxxx")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The account to add the payout method for, prefixed `biz_`. Provide this or `user_id`.
    
</dd>
</dl>

<dl>
<dd>

**destinationCurrency:** `Optional<String>` — Currency the supported payout method delivers payouts in.
    
</dd>
</dl>

<dl>
<dd>

**fields:** `Optional<Map<String, String>>` — The supported payout method's required field values, keyed by field id — list them with `GET /payouts/supported_methods?supported_payout_method_id=...`. Field ids are stable `fld_` identifiers you may hardcode; they never change for a given field. A Basis Theory token id may be passed in place of a raw value. For a U.S. bank routing-number field, a raw nine-digit value must also pass the ABA checksum. A validation failure returns the method's full required_fields schema alongside the error. Required whenever the account details are supplied directly.
    
</dd>
</dl>

<dl>
<dd>

**isDefault:** `Optional<Boolean>` — Whether to make this the account's default payout method.
    
</dd>
</dl>

<dl>
<dd>

**nickname:** `Optional<String>` — A label for the payout method, unique per destination.
    
</dd>
</dl>

<dl>
<dd>

**supportedPayoutMethodId:** `String` — The supported payout method to save (a podst_ identifier from a previous listing).
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — The user to add the payout method for, prefixed `user_`. Provide this or `account_id`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.payouts.methods.delete(id) -> DeleteMethodsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Deletes a saved payout method so it can no longer receive payouts.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.payouts().methods().delete(
    "id",
    DeleteMethodsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Payout method ID, prefixed `potk_`.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.payouts.methods.update(id, request) -> UpdateMethodsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Changes the label used to identify a saved payout method.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.payouts().methods().update(
    "id",
    UpdateMethodsRequest
        .builder()
        .nickname("Primary checking")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Payout method ID, prefixed `potk_`.
    
</dd>
</dl>

<dl>
<dd>

**nickname:** `String` — New label for the payout method, with at least one non-whitespace character and a maximum of 100 characters.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Payouts SupportedMethods
<details><summary><code>client.payouts.supportedMethods.list() -> SyncPagingIterable&amp;lt;ListSupportedMethodsResponseDataItem&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the payout methods an account or user is eligible to add.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.payouts().supportedMethods().list(
    ListSupportedMethodsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — The owning account ID (a biz_ identifier). Provide this or user_id.
    
</dd>
</dl>

<dl>
<dd>

**userId:** `Optional<String>` — The owning user ID (a user_ identifier). Provide this or account_id.
    
</dd>
</dl>

<dl>
<dd>

**country:** `Optional<String>` — ISO 3166-1 alpha-2 country code for the bank account or wallet, such as `US`. Defaults to the country of supported_payout_method_id when one is given, otherwise the payout account's country.
    
</dd>
</dl>

<dl>
<dd>

**amount:** `Optional<Double>` — Optional withdrawal amount in whole currency units, for example `250.00`. When provided, each destination includes per-currency fee and delivery quotes.
    
</dd>
</dl>

<dl>
<dd>

**currency:** `Optional<String>` — Currency code of the amount, for example `usd`. Only meaningful with amount.
    
</dd>
</dl>

<dl>
<dd>

**supportedPayoutMethodId:** `Optional<String>` — Narrows the list to one supported payout method (a podst_ identifier) and includes the required_fields needed to save it as a payout method.
    
</dd>
</dl>

<dl>
<dd>

**destinationCurrency:** `Optional<String>` — Currency the supported payout method would deliver payouts in. Only meaningful with supported_payout_method_id; required fields vary by destination currency.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — Number of supported payout methods to return from the start of the window.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — Cursor to fetch the page after (from page_info.end_cursor).
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — Number of supported payout methods to return from the end of the window.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — Cursor to fetch the page before (from page_info.start_cursor).
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Users OauthGrants
<details><summary><code>client.users.oauthGrants.list() -> SyncPagingIterable&amp;lt;OauthGrant&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the authenticated user's own OAuth grants — one per app they have authorized, per account they authorized it for. The list is always the caller's own; there is no parameter for reading another user's grants. Requires a user session: an API key or an OAuth token is refused, so an app can never enumerate the other apps a user has authorized.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.users().oauthGrants().list(
    ListOauthGrantsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**appId:** `Optional<String>` — Only return grants for this app, prefixed `app_`. An app the user has never authorized returns an empty list.
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of grants to return (default 20, max 100).
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor; returns grants after this position.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — The number of grants to return from the end of the range.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — A cursor; returns grants before this position.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListOauthGrantsRequestOrder>` — The field to sort grants by.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListOauthGrantsRequestDirection>` — Sort direction.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.users.oauthGrants.create(request) -> OauthGrant</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Completes the OAuth authorization step for the authenticated user: records their consent for the scopes an app asked for and mints the authorization code to hand back to it. Returns the grant, plus a `redirect_url` carrying that code — the one and only time it is returned. Exchange the code at `POST /oauth/token` with the verifier for `code_challenge`. Requires a user session, because consent has to come from the account holder: an API key or an OAuth token is refused, so an app can never authorize itself. Send an `Idempotency-Key` to make a retry safe — a replay returns the original `redirect_url` and its code rather than issuing a second one.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.users().oauthGrants().create(
    CreateOauthGrantsRequest
        .builder()
        .clientId("app_xxxxxxxxxxxxxx")
        .codeChallenge("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
        .codeChallengeMethod(CreateOauthGrantsRequestCodeChallengeMethod.S256)
        .redirectUri("https://Booking.Shinetime.example:8443/oauth/Callback/")
        .requestedScopes(
            Arrays.asList("profile")
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**accountId:** `Optional<String>` — Authorize the app for one of the user's accounts rather than for the user alone, prefixed `biz_`. The user must have access to it.
    
</dd>
</dl>

<dl>
<dd>

**clientId:** `String` — The app being authorized, prefixed `app_`.
    
</dd>
</dl>

<dl>
<dd>

**codeChallenge:** `String` — The PKCE code challenge: the base64url-encoded SHA-256 of your code verifier, without padding.
    
</dd>
</dl>

<dl>
<dd>

**codeChallengeMethod:** `CreateOauthGrantsRequestCodeChallengeMethod` — How `code_challenge` was derived. Only `S256` is accepted.
    
</dd>
</dl>

<dl>
<dd>

**consentShown:** `Optional<Boolean>` — Whether the consent UI listed these scopes for the user. Sending `false` succeeds only when the user has already granted every scope requested.
    
</dd>
</dl>

<dl>
<dd>

**nonce:** `Optional<String>` — OIDC nonce, echoed into the resulting ID token. Required when `requested_scopes` includes `openid`.
    
</dd>
</dl>

<dl>
<dd>

**redirectUri:** `String` — Where to send the user once they have consented. Must match one of the app's registered redirect URIs exactly — it is compared as a string, not normalized.
    
</dd>
</dl>

<dl>
<dd>

**requestedScopes:** `List<String>` — The permissions the app is asking for, for example `member:basic:read`. `GET /api_keys/permissions` names and describes each one. Granting adds to whatever the user already granted this app rather than replacing it.
    
</dd>
</dl>

<dl>
<dd>

**responseType:** `Optional<CreateOauthGrantsRequestResponseType>` — The OAuth response type. Only `code` is accepted; defaults to `code`.
    
</dd>
</dl>

<dl>
<dd>

**state:** `Optional<String>` — Opaque value appended to `redirect_url` unchanged, for the client to correlate the response with its request.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Users Passkeys
<details><summary><code>client.users.passkeys.list() -> SyncPagingIterable&amp;lt;Passkey&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the authenticated user's own passkeys, newest first. The list is always the caller's own; there is no parameter for reading another user's passkeys. Requires a user session: an API key or an OAuth token is refused, because a passkey confirms the account holder before a sensitive action and no app may enumerate one.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.users().passkeys().list(
    ListPasskeysRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of passkeys to return (default 20, max 100).
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor; returns passkeys after this position.
    
</dd>
</dl>

<dl>
<dd>

**last:** `Optional<Integer>` — The number of passkeys to return from the end of the range.
    
</dd>
</dl>

<dl>
<dd>

**before:** `Optional<String>` — A cursor; returns passkeys before this position.
    
</dd>
</dl>

<dl>
<dd>

**order:** `Optional<ListPasskeysRequestOrder>` — The field to sort passkeys by.
    
</dd>
</dl>

<dl>
<dd>

**direction:** `Optional<ListPasskeysRequestDirection>` — Sort direction.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.users.passkeys.create(request) -> Passkey</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Registers a passkey for the authenticated user from the attestation a browser produced for a `registration` challenge. Mint that challenge first with `POST /users/me/passkeys/challenge`; it is single-use and expires 5 minutes after it is issued. Requires a user session.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.users().passkeys().create(
    CreatePasskeysRequest
        .builder()
        .attestationObject("YXR0ZXN0YXRpb24")
        .clientDataJson("Y2xpZW50LWRhdGE")
        .credentialId("bmV3LWNyZWRlbnRpYWw")
        .nickname("Work laptop")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**attestationObject:** `String` — The `attestationObject` from the WebAuthn attestation response, base64url-encoded.
    
</dd>
</dl>

<dl>
<dd>

**clientDataJson:** `String` — The `clientDataJSON` from the WebAuthn attestation response, base64url-encoded.
    
</dd>
</dl>

<dl>
<dd>

**credentialId:** `String` — The WebAuthn credential ID the authenticator returned, base64url-encoded.
    
</dd>
</dl>

<dl>
<dd>

**nickname:** `String` — A name for this passkey, usually the device it lives on. 255 characters or fewer.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.users.passkeys.challenge(request) -> ChallengePasskeysResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Mints the challenge a browser needs to run a WebAuthn ceremony against the authenticated user's own passkeys. A `registration` challenge enrolls a new passkey; a `deletion` challenge is bound to the one passkey named by `passkey_id` and proves the user still holds it. Challenges are single-use and expire 5 minutes after they are issued, so send a fresh `Idempotency-Key` per ceremony — a replayed key returns the original challenge, which may already have expired. Requires a user session.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.users().passkeys().challenge(
    ChallengePasskeysRequest
        .builder()
        .challengeType(ChallengePasskeysRequestChallengeType.REGISTRATION)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**challengeType:** `ChallengePasskeysRequestChallengeType` — The ceremony this challenge is for.
    
</dd>
</dl>

<dl>
<dd>

**passkeyId:** `Optional<String>` — The passkey the ceremony targets, prefixed `wcred_`. Required when `challenge_type` is `deletion`, ignored otherwise.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.users.passkeys.delete(id, request) -> DeletePasskeysResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Deletes one of the authenticated user's own passkeys. The request body carries a WebAuthn assertion from the passkey being deleted, so possession of the credential is proven before it is removed: mint a `deletion` challenge for it first, run the ceremony with that passkey, and send the result here. Deleting the user's last passkey is allowed — their other step-up factors remain. Requires a user session.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.users().passkeys().delete(
    "id",
    DeletePasskeysRequest
        .builder()
        .authenticatorData("YXV0aGVudGljYXRvci1kYXRh")
        .clientDataJson("Y2xpZW50LWRhdGE")
        .signature("c2lnbmF0dXJl")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**id:** `String` — Passkey ID, prefixed `wcred_`.
    
</dd>
</dl>

<dl>
<dd>

**authenticatorData:** `String` — The `authenticatorData` from the WebAuthn assertion, base64url-encoded.
    
</dd>
</dl>

<dl>
<dd>

**clientDataJson:** `String` — The `clientDataJSON` from the WebAuthn assertion, base64url-encoded.
    
</dd>
</dl>

<dl>
<dd>

**signature:** `String` — The `signature` from the WebAuthn assertion, base64url-encoded.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Users Preferences
<details><summary><code>client.users.preferences.retrieve() -> UserPreferences</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Retrieves the authenticated user's settings document. Addressed only as `me` — the document always belongs to the session user.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.users().preferences().retrieve();
```
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.users.preferences.update(request) -> UserPreferences</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Updates the authenticated user's settings document. Replaces the top-level keys it is given and leaves the rest untouched.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.users().preferences().update(
    UpdatePreferencesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**bountyWorkerOnboardingDismissed:** `Optional<Boolean>` — Whether the user has dismissed the first-time bounty worker onboarding. Set to `false` to show it again.
    
</dd>
</dl>

<dl>
<dd>

**investigationEnabled:** `Optional<Boolean>` — Whether investigation mode is enabled for the user. Only meaningful for staff users with investigation access.
    
</dd>
</dl>

<dl>
<dd>

**termsAccepted:** `Optional<Boolean>` — Records the user's acceptance of Whop's terms and policies. Only `true` is accepted — the server stamps `terms_accepted_at` and acceptance cannot be withdrawn here.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Users Preferences Notifications
<details><summary><code>client.users.preferences.notifications.set(request) -> SetNotificationsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Sets the authenticated user's notification preferences. Each preference is addressed by `scope`, not by id, so a scope read back from either list endpoint can be sent straight here.

A scope naming an experience with no topic sets that experience's level, and accepts all three levels. Any other scope sets a topic override, which is binary — `all` or `nothing` — and requires a `channel`.

`level: null` clears the preference. Preferences are stored as overrides, so clearing one means the scope inherits its default again rather than being switched off.

The batch is applied in one transaction: if any entry is rejected, none are written. Experience levels are applied before topic overrides, because setting a level replaces every topic preference for that experience — so an override sent alongside a level wins. The response reports what each scope now resolves to, in the order the entries were sent.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.users().preferences().notifications().set(
    SetNotificationsRequest
        .builder()
        .preferences(
            Arrays.asList(
                SetNotificationsRequestPreferencesItem
                    .builder()
                    .level(
                        Nullable.ofNull()
                    )
                    .scope(
                        SetNotificationsRequestPreferencesItemScope
                            .builder()
                            .build()
                    )
                    .build()
            )
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**preferences:** `List<SetNotificationsRequestPreferencesItem>` — The preferences to set, at most 100 per request.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Users Preferences Notifications Experiences
<details><summary><code>client.users.preferences.notifications.experiences.list() -> SyncPagingIterable&amp;lt;ExperienceNotificationPreference&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the authenticated user's per-experience notification levels. Experiences the user never set a level for are omitted — their effective level is `all`.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.users().preferences().notifications().experiences().list(
    ListExperiencesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of preferences to return.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor; returns preferences after this position.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## Users Preferences Notifications Topics
<details><summary><code>client.users.preferences.notifications.topics.list() -> SyncPagingIterable&amp;lt;UserNotificationPreference&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Lists the authenticated user's topic-scoped notification preferences, plus user-agnostic platform defaults. Each filter matches preferences scoped to its value or not narrowed on that dimension. Per-experience levels are listed separately, by `GET /users/me/preferences/notifications/experiences`.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.users().preferences().notifications().topics().list(
    ListTopicsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**channel:** `Optional<ListTopicsRequestChannel>` — Only return preferences for this delivery channel (or not narrowed to a channel).
    
</dd>
</dl>

<dl>
<dd>

**accountId:** `Optional<String>` — Only return preferences scoped to this account's member notifications (`biz_` tag).
    
</dd>
</dl>

<dl>
<dd>

**teamAccountId:** `Optional<String>` — Only return preferences scoped to this account's team notifications (`biz_` tag).
    
</dd>
</dl>

<dl>
<dd>

**experienceId:** `Optional<String>` — Only return preferences scoped to this experience (`exp_` tag).
    
</dd>
</dl>

<dl>
<dd>

**topicId:** `Optional<String>` — Only return preferences scoped to this notification topic (`topic_` tag).
    
</dd>
</dl>

<dl>
<dd>

**first:** `Optional<Integer>` — The number of preferences to return.
    
</dd>
</dl>

<dl>
<dd>

**after:** `Optional<String>` — A cursor; returns preferences after this position.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

