"use client";

import {
  Table,
  TableHeader,
  TableBody,
  TableColumn,
  TableRow,
  TableCell, getKeyValue
} from "@nextui-org/table";

import {Input} from "@nextui-org/input";
import {Button} from "@nextui-org/button";
import {SearchIcon, PlusIcon, EditIcon, DeleteIcon} from "@/components/icons";

export interface Column {
  key: string;
  label: string;
}

export interface DataGridProps {
  /** DataGrid header */
  header: string;
  /** DataGrid columns */
  columns: Column[];
  /** DataGrid data */
  data: any[];
}

export const DataGrid = ({header, columns, data}: DataGridProps) => {
  return (
    <section className="flex flex-col gap-4">
      <h1>{header}</h1>
      <Table
        selectionMode="single"
        topContent={<ControlPanel/>}
        topContentPlacement="outside"
      >
        <TableHeader columns={columns}>
          {(column) => <TableColumn key={column.key}>{column.label}</TableColumn>}
        </TableHeader>
        <TableBody items={data}>
          {(item) => (
            <TableRow key={item.id}>
              {(columnKey) => <TableCell>{getKeyValue(item, columnKey)}</TableCell>}
            </TableRow>
          )}
        </TableBody>
      </Table>
    </section>
  );
};

export const ControlPanel = () => {
  return (
    <div className="flex justify-between">
      <div className="flex justify-start items-center gap-4">
        <Button startContent={<PlusIcon/>} color="primary">Add</Button>
        <Button startContent={<EditIcon/>} color="secondary">Edit</Button>
        <Button startContent={<DeleteIcon/>} color="danger">Delete</Button>
      </div>
      <div className="flex justify-end items-center gap-4">
        <Input
          aria-label="Search"
          classNames={{
            inputWrapper: "bg-default-100",
            input: "text-sm",
          }}
          labelPlacement="outside"
          placeholder="Search..."
          startContent={
            <SearchIcon className="text-base text-default-400 pointer-events-none flex-shrink-0"/>
          }
          type="search"
        />
      </div>
    </div>
  );
};
