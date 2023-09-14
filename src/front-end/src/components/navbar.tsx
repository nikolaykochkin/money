"use client";

import {
  Navbar as NextUINavbar,
  NavbarContent,
  NavbarBrand,
  NavbarItem,
} from "@nextui-org/navbar";

import {
  Dropdown,
  DropdownTrigger,
  DropdownMenu,
  DropdownItem,
} from "@nextui-org/dropdown";

import {Button} from "@nextui-org/button";
import {User} from "@nextui-org/user";

import NextLink from "next/link";

import {siteConfig} from "@/config/site";

import {ThemeSwitch} from "@/components/theme-switch";
import {Logo, ChevronDown} from "@/components/icons";

export const Navbar = () => {
  return (
    <NextUINavbar maxWidth="xl" position="sticky">
      <NavbarBrand>
        <NextLink className="flex justify-start items-center gap-1" href="/">
          <Logo/>
          <p className="font-bold text-inherit">Money</p>
        </NextLink>
      </NavbarBrand>

      <NavbarContent className="gap-4" justify="center">
        {siteConfig.navMenuItems.map((item) => (
          <Dropdown key={item.key}>
            <NavbarItem>
              <DropdownTrigger>
                <Button
                  disableRipple
                  className="p-0 bg-transparent data-[hover=true]:bg-transparent"
                  endContent={<ChevronDown fill="currentColor" size={16}/>}
                  radius="sm"
                  variant="light"
                >
                  {item.label}
                </Button>
              </DropdownTrigger>
            </NavbarItem>
            <DropdownMenu>
              {item.dropdownItems.map((dropdown) => (
                <DropdownItem key={dropdown.key}>
                  <NextLink href={dropdown.href}>
                    {dropdown.label}
                  </NextLink>
                </DropdownItem>
              ))}
            </DropdownMenu>
          </Dropdown>
        ))}
      </NavbarContent>

      <NavbarContent justify="end">
        <NavbarItem>
          <ThemeSwitch/>
        </NavbarItem>
        <NavbarItem>
          <User
            name="User"
            description="User description"
            avatarProps={{
              src: "https://i.pravatar.cc/150?img=11"
            }}
          />
        </NavbarItem>
      </NavbarContent>
    </NextUINavbar>
  );
};
