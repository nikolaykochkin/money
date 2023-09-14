import {DataGrid} from "@/components/datagrid";
import {userService} from "@/api/user/user-service";

export default async function Home() {
  const users = await userService.getAll();
  const columns = [
    {
      key: "name",
      label: "NAME",
    },
    {
      key: "login",
      label: "LOGIN",
    },
    {
      key: "telegramId",
      label: "TELEGRAM ID",
    },
  ];
  return (
    <DataGrid header="Users" columns={columns} data={users}/>
  )
}
