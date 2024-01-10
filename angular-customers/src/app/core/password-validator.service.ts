import { Injectable } from '@angular/core';
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class PasswordValidatorService {
  static match(controlName: string, controlNameToMatch: string): ValidatorFn {
    return (group: AbstractControl): ValidationErrors | null => {
      const control = group.get(controlName);
      const controlToMatch = group.get(controlNameToMatch);

      if (!control || !controlToMatch) {
        console.error('The control can not be found in the form group');
        return { controlNotFound: false };
      }

      const error =
        control.value === controlToMatch.value ? null : { noMatch: true };

      controlToMatch.setErrors(error);

      return error;
    };
  }
}
