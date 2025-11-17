import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Footer } from './core/layout/footer';
import { Header } from './core/layout/header';

@Component({
  selector: 'app-root',
  imports: [Header,RouterOutlet,Footer],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('conduit-frontend');
}
