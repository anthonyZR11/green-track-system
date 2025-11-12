import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { authInterceptor } from './core/auth.interceptor';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideToastr } from 'ngx-toastr';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideClientHydration(),
    provideAnimations(),
    provideToastr({
      positionClass: 'toast-top-right', // Cambia de full-width a right o corner
      timeOut: 3000,
      progressBar: true,
      closeButton: true,
      toastClass: 'ngx-toastr' // Clase personalizada para estilos adicionales
    }),
    provideHttpClient(
      withInterceptors([
        authInterceptor
      ])
    )
  ]
};
