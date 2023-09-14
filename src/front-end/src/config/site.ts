export type SiteConfig = typeof siteConfig;
export const siteConfig = {
  name: "Money",
  description: "Personal finance web application",
  apiBaseUrl: "http://127.0.0.1:8080",
  navMenuItems: [
    {
      label: "Transaction",
      key: "transaction",
      dropdownItems: [
        {
          label: "Transactions",
          key: "transactions",
          href: "/transactions",
        },
        {
          label: "Movements",
          key: "movements",
          href: "/movements",
        },
        {
          label: "Accounts",
          key: "accounts",
          href: "/accounts",
        },
        {
          label: "Categories",
          key: "categories",
          href: "/categories",
        },
      ],
    },
    {
      label: "Invoice",
      key: "invoice",
      dropdownItems: [
        {
          label: "Invoices",
          key: "invoices",
          href: "/invoices",
        },
        {
          label: "Items",
          key: "items",
          href: "/items",
        },
        {
          label: "Sellers",
          key: "sellers",
          href: "/sellers",
        },
      ],
    },
    {
      label: "Report",
      key: "report",
      dropdownItems: [
        {
          label: "Balance",
          key: "balance",
          href: "/balance",
        },
      ],
    },
    {
      label: "Admin",
      key: "admin",
      dropdownItems: [
        {
          label: "Users",
          key: "users",
          href: "/users",
        },
      ],
    },
  ],
};
