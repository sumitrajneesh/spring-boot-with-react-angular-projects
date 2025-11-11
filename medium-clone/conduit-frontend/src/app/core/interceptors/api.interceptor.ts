import { HttpInterceptorFn } from "@angular/common/http";
// import { environment } from "../../../environments/environment";

// export const apiInterceptor: HttpInterceptorFn = (req, next) => {
//   const apiReq = req.clone({ url: `${environment.api_url}${req.url}` });
//   return next(apiReq);
// };

export const apiInterceptor: HttpInterceptorFn = (req, next) => {
  const apiReq = req.clone({ url: `https://api.realworld.show/api${req.url}` });
  return next(apiReq);
};
