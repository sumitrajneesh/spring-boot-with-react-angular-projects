import { Component, inject } from "@angular/core";
import { UserService } from "../auth/services/user";
import { RouterLink, RouterLinkActive } from "@angular/router";
import { AsyncPipe } from "@angular/common";
import { IfAuthenticatedDirective } from "../auth/if-authenticated";

@Component({
  selector: 'app-layout-header',
   imports: [RouterLinkActive, RouterLink, AsyncPipe, IfAuthenticatedDirective],
  templateUrl: './header.html'
})
export class Header {
 currentUser$ = inject(UserService).currentUser;
}
