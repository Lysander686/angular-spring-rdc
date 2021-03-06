import { MessageService } from './message.service';
import { Injectable } from '@angular/core';
import { Hero } from './hero';
import { HEROES } from './mock-heroes';
import { Observable, of } from 'rxjs';

@Injectable({
  // provide the service at the root level, creates a single, shared instance of HeroService and injects into any class that asks for it.
  providedIn: 'root',
})
export class HeroService {
  constructor(private messageService: MessageService) { }
   getHeroes(): Observable<Hero[]>  {
    const heroes = of(HEROES);

     this.messageService.add('HeroService: fetched heroes success');

    return heroes;
  }
}
