import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMarketSector } from 'app/shared/model/market-sector.model';
import { MarketSectorService } from './market-sector.service';

@Component({
    selector: 'jhi-market-sector-delete-dialog',
    templateUrl: './market-sector-delete-dialog.component.html'
})
export class MarketSectorDeleteDialogComponent {
    marketSector: IMarketSector;

    constructor(
        private marketSectorService: MarketSectorService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.marketSectorService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'marketSectorListModification',
                content: 'Deleted an marketSector'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-market-sector-delete-popup',
    template: ''
})
export class MarketSectorDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ marketSector }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MarketSectorDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.marketSector = marketSector;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
