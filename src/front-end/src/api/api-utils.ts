import {siteConfig} from "@/config/site";

const baseUrl = siteConfig.apiBaseUrl;

export async function get<TResponse>(url: string): Promise<TResponse> {
  return request<TResponse>(`${baseUrl}/${url}`)
}

export async function post<TResponse>(url: string, body: any): Promise<TResponse> {
  return request<TResponse>(`${baseUrl}/${url}`, {
    method: 'POST',
    headers: {
      'content-type': 'application/json;charset=UTF-8',
    },
    body: body,
  })
}

export async function put<TResponse>(url: string, body: any): Promise<TResponse> {
  return request<TResponse>(`${baseUrl}/${url}`, {
    method: 'PUT',
    headers: {
      'content-type': 'application/json;charset=UTF-8',
    },
    body: body,
  })
}

export async function request<TResponse>(
  url: string,
  config: RequestInit | undefined = undefined
): Promise<TResponse> {
  try {
    const response = await fetch(url, config)
    return await response.json()
  } catch (error) {
    console.log("Something went wrong")
    return Promise.reject(error)
  }
}