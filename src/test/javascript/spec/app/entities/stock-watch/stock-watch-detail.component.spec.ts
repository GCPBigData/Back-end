/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClivServerTestModule } from '../../../test.module';
import { StockWatchDetailComponent } from 'app/entities/stock-watch/stock-watch-detail.component';
import { StockWatch } from 'app/shared/model/stock-watch.model';

describe('Component Tests', () => {
    describe('StockWatch Management Detail Component', () => {
        let comp: StockWatchDetailComponent;
        let fixture: ComponentFixture<StockWatchDetailComponent>;
        const route = ({ data: of({ stockWatch: new StockWatch(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClivServerTestModule],
                declarations: [StockWatchDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(StockWatchDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(StockWatchDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.stockWatch).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
