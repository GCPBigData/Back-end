/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ClivServerTestModule } from '../../../test.module';
import { MarketSectorUpdateComponent } from 'app/entities/market-sector/market-sector-update.component';
import { MarketSectorService } from 'app/entities/market-sector/market-sector.service';
import { MarketSector } from 'app/shared/model/market-sector.model';

describe('Component Tests', () => {
    describe('MarketSector Management Update Component', () => {
        let comp: MarketSectorUpdateComponent;
        let fixture: ComponentFixture<MarketSectorUpdateComponent>;
        let service: MarketSectorService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClivServerTestModule],
                declarations: [MarketSectorUpdateComponent]
            })
                .overrideTemplate(MarketSectorUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(MarketSectorUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MarketSectorService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new MarketSector(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.marketSector = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new MarketSector();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.marketSector = entity;
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
