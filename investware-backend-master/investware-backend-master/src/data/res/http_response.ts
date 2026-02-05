type TData = string | boolean | object | object[];

export class HttpResponse {
  public data: TData;
  public error: boolean;

  constructor(data: TData, error: boolean) {
    this.data = data;
    this.error = error;
  }
}
