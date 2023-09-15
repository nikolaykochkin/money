import {get, put, post} from "@/api/api-utils";
import {User} from "./user";

const url: string = "user"

export const userService = {
  getAll: () => get<User[]>(url),
  getById: (id: bigint) => get<User>(`${url}/${id}`),
  create: (user: User) => post(url, user),
  update: (user: User) => put(`${url}/${user.id}`, user),
}