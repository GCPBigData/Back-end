/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ClivServerTestModule } from '../../../test.module';
import { StockWatchUpdateComponent } from 'app/entities/stock-watch/stock-watch-update.component';
import { StockWatchService } from 'app/entities/stock-watch/stock-watch.service';
import { StockWatch } from 'app/shared/model/stock-watch.model';

describe('Component Tests', () => {
    describe('StockWatch Management Update Component', () => {
        let comp: StockWatchUpdateComponent;
        let fixture: ComponentFixture<StockWatchUpdateComponent>;
        let service: StockWatchService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClivServerTestModule],
                declarations: [StockWatchUpdateComponent]
            })
                .overrideTemplate(StockWatchUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(StockWatchUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StockWatchService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new StockWatch(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.stockWatch = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new StockWatch();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.stockWatch = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
